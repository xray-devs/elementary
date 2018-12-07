package com.elementary.core.dao;

import com.elementary.core.modle.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Mr丶s
 * @ClassName UserDao
 * @Version V1.0
 * @Date 2018/12/6 17:38
 * @Description
 */
@Mapper
public interface UserDao extends BaseMapper<User>{

}
