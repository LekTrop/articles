package ua.hnure.zhytariuk.models.mapper;


import org.mapstruct.Mapper;
import ua.hnure.zhytariuk.models.api.UserApi;
import ua.hnure.zhytariuk.models.domain.User;
import ua.hnure.zhytariuk.models.mapper.config.MapStructConfig;

@Mapper(
        config = MapStructConfig.class,
        uses = {
                RoleMapper.class
        }
)
public interface UserMapper {
    UserApi toApi(final User user);

    User toDomain(final UserApi userApi);
}
