package com.oz.userservice.models.mapper;

import com.oz.userservice.models.api.UserApi;
import com.oz.userservice.models.domain.User;
import com.oz.userservice.models.mapper.config.MapStructAuthConfig;
import org.mapstruct.Mapper;

@Mapper(
        config = MapStructAuthConfig.class,
        uses = {
                RoleMapper.class
        }
)
public interface UserMapper {
    UserApi toApi(final User user);

    User toDomain(final UserApi userApi);
}
