package com.elementary.core.modle;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Mr丶s
 * @ClassName BaseModle
 * @Version V1.0
 * @Date 2018/12/6 17:18
 * @Description
 */
@Data
public class BaseModle implements Serializable {

    /**
     * 创建时间
     */
    protected Date gmtCreated;

    /**
     * 修改时间
     */
    protected Date gmtUpdated;

    public String toJson(){
        return JSON.toJSONString(this);
    }

}
