package com.cxin.cxintemplate.infrastructure.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxin.cxintemplate.infrastructure.model.dto.user.UserUpdateRequest;
import com.cxin.cxintemplate.infrastructure.model.entity.User;
import com.cxin.cxintemplate.infrastructure.model.vo.user.LoginUserVO;
import com.cxin.cxintemplate.infrastructure.model.vo.user.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserConvert {
    public abstract LoginUserVO userToLoginUserVO(User user);

    public abstract UserVO userToUserVO(User user);

    public abstract Page<UserVO> userPageToUserVoPage(Page<User> userPage);

    public abstract User userUpdateRequestToUser(UserUpdateRequest userUpdateRequest);

}
