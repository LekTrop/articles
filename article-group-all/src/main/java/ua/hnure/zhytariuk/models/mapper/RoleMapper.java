package ua.hnure.zhytariuk.models.mapper;

import org.mapstruct.Mapper;
import ua.hnure.zhytariuk.models.api.RoleApi;
import ua.hnure.zhytariuk.models.domain.user.Role;
import ua.hnure.zhytariuk.models.mapper.config.MapStructConfig;

@Mapper(config = MapStructConfig.class)
public interface RoleMapper {

    Role toDomain(final RoleApi roleApi);

    RoleApi toApi(final Role role);
}
