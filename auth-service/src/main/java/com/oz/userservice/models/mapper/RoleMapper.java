package com.oz.userservice.models.mapper;

import com.oz.userservice.models.api.RoleApi;
import com.oz.userservice.models.domain.Role;
import com.oz.userservice.models.mapper.config.MapStructAuthConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructAuthConfig.class)
public interface RoleMapper {

    Role toDomain(final RoleApi roleApi);

    RoleApi toApi(final Role role);
}
