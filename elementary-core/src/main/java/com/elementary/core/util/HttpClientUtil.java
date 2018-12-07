package com.elementary.core.util;

import com.alibaba.fastjson.JSON;
import com.elementary.core.enums.StatusEnum;
import com.elementary.core.result.Result;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * @author Mr丶s
 * @ClassName HTTP请求
 * @Version V1.0
 * @Date 2018/12/6 10:38
 * @Description
 */
@Slf4j
@Component
public class HttpClientUtil{

    private static final String APPLICATION_JSON = "application/json";

    private static List<String> COOKIES = new ArrayList<>();

    //链接超时秒
    private final static int CONNECT_TIMEOUT = 4 * 1000;

    //请求超时秒
    private final static int CONNECTION_REQUEST_TIMEOUT = 4 * 1000;

    //请求获取数据的超时时间
    private final static int SOCKET_TIMEOUT = 4 * 1000;


    /**
     * 普通请求，请求参数不是json
     *
     * @param url    url
     * @param params params
     * @param clazz  返回值类型
     * @return 结果
     */
    public static Result commonPost(String url, Map<String, String> params, Class clazz) {
        return commonPost(url, params, clazz, null);
    }

    /**
     * 普通请求，请求参数不是json
     *
     * @param url    url
     * @param params params
     * @param clazz  返回值类型
     * @return 结果
     */
    public static Result commonPost(String url, Map<String, String> params, Class clazz, Map<String, String> headers) {
        return commonPost(url, params, clazz, headers, CONNECT_TIMEOUT);
    }


    /**
     * 普通请求，请求参数不是json
     *
     * @param url    url
     * @param params params
     * @param clazz  返回值类型
     * @return 结果
     */
    public static Result commonPost(String url, Map<String, String> params, Class clazz, Map<String, String> headers, int timeout) {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        HttpPost httppost = new HttpPost(getHttpUrl(url));

        //setConnectTimeout：设置连接超时时间，单位毫秒.setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间.setSocketTimeout：请求获取数据的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
        httppost.setConfig(requestConfig);

        setHeaders(httppost, headers);

        Result result;
        CloseableHttpResponse response = null;

        if (COOKIES != null && !COOKIES.isEmpty()) {
            for (String cookie : COOKIES) {
                httppost.addHeader("Cookie", cookie);
            }
        }

        List<NameValuePair> pairs = null;
        if (params != null && !params.isEmpty()) {
            pairs = new ArrayList<>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
        }

        try {
            httppost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));

            response = httpclient.execute(httppost);

            result = response2object(response, clazz, url, params);
        } catch (SocketTimeoutException e) {
            log.error("commonPost error,url=" + url + ",params=" + params, e);
            result = Result.fail(StatusEnum.NET_TIMEOUT_ERROR);
        } catch (Throwable e) {
            log.error("commonPost error,url=" + url + ",params=" + params, e);
            result = Result.fail(StatusEnum.NET_ERROR);
        } finally {
            // 关闭连接,释放资源
            closeAbleHttpResponse(response, httpclient, url, params);
        }

        return result;
    }

    /**
     * 关闭
     *
     * @param response 返回response
     */
    private static void closeAbleHttpResponse(CloseableHttpResponse response, CloseableHttpClient httpclient, String url, Object params) {
        try {
            if (null != response) {
                response.close();
            }
            if (null != httpclient) {
                httpclient.close();
            }
        } catch (IOException e) {
            log.error("httpclient.close error,url=" + url + ",params=" + params, e);
        }
    }

    /**
     * 请求参数按照json形式调用业务方的http接口
     *
     * @param url    url
     * @param params params
     * @param clazz  clazz
     * @return 结果
     */
    public static Result jsonPost(String url, Object params, Class clazz) {
        return jsonPost(url, params, clazz, SOCKET_TIMEOUT);
    }

    /**
     * 请求参数按照json形式调用业务方的http接口
     *
     * @param url     地址
     * @param params  参数
     * @param clazz   转化类型
     * @param timeout 超时时间
     * @return 结果
     */
    public static Result jsonPost(String url, Object params, Class clazz, int timeout) {
        return jsonPost(url, params, clazz, null, timeout);
    }

    /**
     * 请求参数按照json形式调用业务方的http接口
     *
     * @param url     地址
     * @param params  参数
     * @param clazz   转化类型
     * @param timeout 超时时间
     * @return 结果
     */
    public static Result jsonPost(String url, Object params, Class clazz, Map<String, String> headers, int timeout) {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        HttpPost httppost = new HttpPost(getHttpUrl(url));
        //setConnectTimeout：设置连接超时时间，单位毫秒.setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间.setSocketTimeout：请求获取数据的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
        httppost.setConfig(requestConfig);

        setHeaders(httppost, headers);

        Result result;
        CloseableHttpResponse response = null;

        httppost.setHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
        if (COOKIES != null && !COOKIES.isEmpty()) {
            for (String cookie : COOKIES) {
                httppost.addHeader("Cookie", cookie);
            }
        }
        String jsonParam = JSON.toJSONString(params);
        StringEntity stringEntity = new StringEntity(jsonParam, "UTF-8");
        httppost.setEntity(stringEntity);

        try {
            response = httpclient.execute(httppost);
            result = response2object(response, clazz, url, params);
        } catch (SocketTimeoutException e) {
            log.error("jsonPost error,url=" + url + ",params=" + jsonParam, e);
            result = Result.fail(StatusEnum.NET_TIMEOUT_ERROR);
        } catch (Throwable e) {
            log.error("jsonPost error,url=" + url + ",params=" + jsonParam, e);
            result = Result.fail(StatusEnum.NET_ERROR);
        } finally {
            // 关闭连接,释放资源
            closeAbleHttpResponse(response, httpclient, url, params);
            log.info("url【" + url + "】 param【" + jsonParam + "】");
        }
        return result;
    }

    /**
     * 设置头信息
     *
     * @param post    post客户端
     * @param headers 头信息
     */
    private static void setHeaders(HttpPost post, Map<String, String> headers) {
        if (null == post) {
            return;
        }
        post.setHeader(HTTP.USER_AGENT, getUserAgent());
        if (null == headers) {
            return;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            post.setHeader(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 返回的json转成JAVA对象返回
     *
     * @param response 返回值
     * @param clazz    类型
     * @return 结果
     */
    public static Result response2object(HttpResponse response, Class clazz, String url, Object params) {
        String responseStr = "";
        try {
            HttpEntity entity = response.getEntity();
            responseStr = EntityUtils.toString(entity, "UTF-8");

            //Result格式解析
            if (isResultStr(responseStr)) {
                return parseResultStr(responseStr, clazz);
            }

            Result result = new Result();
            Object entry;

            if (isArray(responseStr)) {
                entry = JSON.parseArray(responseStr,clazz);
            } else {
                entry = JSON.parseObject(responseStr,clazz);
            }

            Result resultSuccess = Result.success(entry);
            resultSuccess.setMessage(result.getMessage());
            resultSuccess.setResponseCode(result.getResponseCode());

            return resultSuccess;
        } catch (Throwable e) {
            log.error("HttpToJavaObject error,url=" + url + ",params=" + JSON.toJSONString(params) + ",responseStr=" + responseStr, e);
            return Result.fail(StatusEnum.NET_ERROR);
        }
    }

    /**
     * 判断返回结果是否已经是Result格式标准
     *
     * @param responseStr 结果字符串
     * @return 结果
     */
    private static boolean isResultStr(String responseStr) {
        if (StringUtils.isEmpty(responseStr)) {
            return false;
        }
        return responseStr.contains("status");
    }

    /**
     * 解析Result格式的字符串
     *
     * @param responseStr 返回结果
     * @param clazz       entry类型
     * @return 解析结果
     */
    private static Result parseResultStr(String responseStr, Class clazz) {
        Result result = JSON.parseObject(responseStr, Result.class);

        if (null == result.getEntry()) {
            return result;
        }
        try {
            Map map = (Map) result.getEntry();
            String mapJson = JSON.toJSONString(map);
            if (StringUtils.isNotEmpty(mapJson)) {
                Object entry = JSON.parseObject(mapJson, clazz);
                result.setEntry(entry);
            }
            return result;
        } catch (Throwable e) {
            log.error("解析异常", e);
            return result;
        }
    }

    /**
     * get请求
     *
     * @param url    url
     * @param params params
     * @return 结果
     */
    public static Result get(String url, Map<String, String> params, Class clazz) {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        HttpGet httpGet;
        //setConnectTimeout：设置连接超时时间，单位毫秒.setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间.setSocketTimeout：请求获取数据的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();

        String paramsStr = "?";
        if (params != null && !params.isEmpty()) {
            Map.Entry<String, String> entry;
            StringBuilder sb = new StringBuilder();
            for (Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator(); iterator.hasNext(); ) {
                entry = iterator.next();
                sb.append(entry.getKey().toString()).append("=").append(null == entry.getValue() ? "" :
                        entry.getValue().toString()).append(iterator.hasNext() ? "&" : "");
            }
            paramsStr += sb.toString();
        }

        String lastUrl = getHttpUrl(url) + paramsStr;
        log.info("http get url={}", lastUrl);
        httpGet = new HttpGet(lastUrl);
        httpGet.setConfig(requestConfig);

        httpGet.setHeader(HTTP.USER_AGENT, getUserAgent());

        Result result;
        CloseableHttpResponse response = null;
        try {
            if (COOKIES != null && !COOKIES.isEmpty()) {
                for (String cookie : COOKIES) {
                    httpGet.addHeader("Cookie", cookie);
                }
            }
            response = httpclient.execute(httpGet);

            result = response2object(response, clazz, url, params);
        } catch (SocketTimeoutException e) {
            log.error("get error,url=" + url + ",params=" + params, e);
            result = Result.fail(StatusEnum.NET_TIMEOUT_ERROR);
        } catch (Throwable e) {
            log.error("get error,url=" + url + ",params=" + params, e);
            result = Result.fail(StatusEnum.NET_ERROR);
        } finally {
            // 关闭连接,释放资源
            closeAbleHttpResponse(response, httpclient, url, params);
        }

        return result;
    }

    /**
     * 传入url不作任何拼接
     *
     * @param url   全地址
     * @param clazz 类型
     * @return 结果
     */
    public static Result<Object> get(String url, Class clazz) {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        HttpGet httpGet;
        //setConnectTimeout：设置连接超时时间，单位毫秒.setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间.setSocketTimeout：请求获取数据的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();

        httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        httpGet.setHeader(HTTP.USER_AGENT, getUserAgent());
        Object entry = null;
        Result<Object> result;
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseStr = EntityUtils.toString(entity, "UTF-8");
            entry = JSON.parseObject(responseStr,clazz);
        } catch (SocketTimeoutException e) {
            log.error("get error,url=" + url, e);
            result = Result.fail(StatusEnum.NET_TIMEOUT_ERROR);
            return result;
        } catch (Throwable e) {
            log.error("get error,url=" + url, e);
            result = Result.fail(StatusEnum.NET_ERROR);
            return result;
        } finally {
            // 关闭连接,释放资源
            closeAbleHttpResponse(response, httpclient, url, "");
        }

        return Result.success(entry);
    }

    /**
     * 获取url前缀
     *
     * @return 拼接结果
     */
    private static String getHttpUrlPrefix() {
        return "";
    }

    /**
     * 获取url
     *
     * @param url url地址
     * @return 地址
     */
    private static String getHttpUrl(String url) {
        return getHttpUrlPrefix() + url;
    }

    /**
     * @return 代理值
     */
    private static String getUserAgent() {
        long time = System.currentTimeMillis();
        return "Apache-HttpClient/4.5.2 (Java/1.7.0_79) time(" + time + ") appType(pos)";
    }


    private static boolean isArray(String json){
        Gson gson = new Gson();
        JsonObject jso = gson.fromJson(json,JsonObject.class);
        jso.isJsonArray();
        return true;
    }


}
