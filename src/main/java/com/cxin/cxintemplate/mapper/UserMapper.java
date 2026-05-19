package com.cxin.cxintemplate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxin.cxintemplate.infrastructure.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author Charles Chen
 * @since 2026-03-21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
