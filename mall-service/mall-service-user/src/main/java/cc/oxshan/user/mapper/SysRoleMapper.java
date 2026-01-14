package cc.oxshan.user.mapper;

import cc.oxshan.user.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 Mapper
 */
@Mapper
public interface SysRoleMapper {

    /**
     * 根据用户ID查询角色列表
     */
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 分页查询角色列表
     */
    List<SysRole> selectPage(@Param("shopId") Long shopId, 
                              @Param("offset") Integer offset, 
                              @Param("limit") Integer limit);

    /**
     * 统计角色总数
     */
    Long countByShopId(@Param("shopId") Long shopId);

    /**
     * 根据ID查询角色
     */
    SysRole selectById(@Param("id") Long id);

    /**
     * 插入角色
     */
    int insert(SysRole role);

    /**
     * 更新角色
     */
    int updateById(SysRole role);

    /**
     * 删除角色
     */
    int deleteById(@Param("id") Long id);

    /**
     * 统计使用该角色的用户数
     */
    Long countUsersByRoleId(@Param("roleId") Long roleId);

    /**
     * 删除角色菜单关联
     */
    int deleteRoleMenus(@Param("roleId") Long roleId);

    /**
     * 批量插入角色菜单关联
     */
    int insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

    /**
     * 查询角色的菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
