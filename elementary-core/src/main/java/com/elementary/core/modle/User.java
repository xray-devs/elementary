package com.elementary.core.modle;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Mr丶s
 * @ClassName User
 * @Version V1.0
 * @Date 2018/12/6 17:35
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModle{

    private Long id;

    private String username;

    private String password;

    @Override
    public String toJson(){
        return JSON.toJSONString(this);
    }


}
