package cc.oxshan.user.mapper;

import cc.oxshan.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 用户 Mapper
 */
@Mapper
public interface SysUserMapper {

    /**
     * 根据用户名查询用户
     */
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询权限标识
     */
    Set<String> selectPermsByUserId(@Param("userId") Long userId);
}
