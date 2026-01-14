package cc.oxshan.user.mapper;

import cc.oxshan.user.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单 Mapper
 */
@Mapper
public interface SysMenuMapper {

    /**
     * 查询所有菜单
     */
    List<SysMenu> selectAll();

    /**
     * 根据ID查询菜单
     */
    SysMenu selectById(@Param("id") Long id);

    /**
     * 插入菜单
     */
    int insert(SysMenu menu);

    /**
     * 更新菜单
     */
    int updateById(SysMenu menu);

    /**
     * 删除菜单
     */
    int deleteById(@Param("id") Long id);

    /**
     * 统计子菜单数量
     */
    Long countChildren(@Param("parentId") Long parentId);

    /**
     * 根据角色ID查询菜单
     */
    List<SysMenu> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询菜单
     */
    List<SysMenu> selectByUserId(@Param("userId") Long userId);
}
