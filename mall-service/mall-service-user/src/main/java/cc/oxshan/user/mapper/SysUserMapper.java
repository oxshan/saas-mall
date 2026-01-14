package cc.oxshan.user.mapper;

import cc.oxshan.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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

    /**
     * 根据ID查询用户
     */
    SysUser selectById(@Param("id") Long id);

    /**
     * 分页查询用户列表
     */
    List<SysUser> selectPage(@Param("shopId") Long shopId, 
                              @Param("offset") Integer offset, 
                              @Param("limit") Integer limit);

    /**
     * 统计用户总数
     */
    Long countByShopId(@Param("shopId") Long shopId);

    /**
     * 插入用户
     */
    int insert(SysUser user);

    /**
     * 更新用户
     */
    int updateById(SysUser user);

    /**
     * 逻辑删除用户
     */
    int deleteById(@Param("id") Long id);

    /**
     * 更新密码
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 删除用户角色关联
     */
    int deleteUserRoles(@Param("userId") Long userId);

    /**
     * 批量插入用户角色关联
     */
    int insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}
