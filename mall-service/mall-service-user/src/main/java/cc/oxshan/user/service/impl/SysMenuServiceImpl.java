package cc.oxshan.user.service.impl;

import cc.oxshan.api.user.SysMenuService;
import cc.oxshan.api.user.dto.SysMenuDTO;
import cc.oxshan.common.core.exception.BizException;
import cc.oxshan.common.core.result.rpc.ListResult;
import cc.oxshan.common.core.result.rpc.PlainResult;
import cc.oxshan.user.entity.SysMenu;
import cc.oxshan.user.mapper.SysMenuMapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现 (Dubbo)
 */
@DubboService
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuMapper menuMapper;

    @Override
    public ListResult<SysMenuDTO> getMenuTree() {
        ListResult<SysMenuDTO> result = new ListResult<>();
        
        // 查询所有菜单
        List<SysMenu> allMenus = menuMapper.selectAll();
        
        // 转换为 DTO
        List<SysMenuDTO> dtoList = allMenus.stream().map(menu -> {
            SysMenuDTO dto = new SysMenuDTO();
            BeanUtils.copyProperties(menu, dto);
            return dto;
        }).collect(Collectors.toList());
        
        // 构建树形结构
        List<SysMenuDTO> tree = buildMenuTree(dtoList, 0L);
        
        result.setData(tree);
        return result;
    }

    @Override
    public PlainResult<SysMenuDTO> getMenuById(Long menuId) {
        PlainResult<SysMenuDTO> result = new PlainResult<>();
        SysMenu menu = menuMapper.selectById(menuId);
        if (menu != null) {
            SysMenuDTO dto = new SysMenuDTO();
            BeanUtils.copyProperties(menu, dto);
            result.setData(dto);
        }
        return result;
    }

    @Override
    public PlainResult<Long> createMenu(SysMenuDTO dto) {
        PlainResult<Long> result = new PlainResult<>();
        
        // 转换为实体
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(dto, menu);
        
        // 插入菜单
        menuMapper.insert(menu);
        
        result.setData(menu.getId());
        return result;
    }

    @Override
    public PlainResult<Void> updateMenu(SysMenuDTO dto) {
        PlainResult<Void> result = new PlainResult<>();
        
        // 转换为实体
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(dto, menu);
        
        // 更新菜单
        menuMapper.updateById(menu);
        
        return result;
    }

    @Override
    public PlainResult<Void> deleteMenu(Long menuId) {
        PlainResult<Void> result = new PlainResult<>();
        
        // 检查是否有子菜单
        Long childCount = menuMapper.countChildren(menuId);
        if (childCount > 0) {
            throw new BizException(500, "该菜单下还有子菜单，无法删除");
        }
        
        // 删除菜单
        menuMapper.deleteById(menuId);
        
        return result;
    }

    @Override
    public ListResult<SysMenuDTO> getMenusByRoleId(Long roleId) {
        ListResult<SysMenuDTO> result = new ListResult<>();
        
        // 查询角色的菜单
        List<SysMenu> menus = menuMapper.selectByRoleId(roleId);
        
        // 转换为 DTO
        List<SysMenuDTO> dtoList = menus.stream().map(menu -> {
            SysMenuDTO dto = new SysMenuDTO();
            BeanUtils.copyProperties(menu, dto);
            return dto;
        }).collect(Collectors.toList());
        
        result.setData(dtoList);
        return result;
    }

    @Override
    public ListResult<SysMenuDTO> getMenusByUserId(Long userId) {
        ListResult<SysMenuDTO> result = new ListResult<>();
        
        // 查询用户的菜单
        List<SysMenu> menus = menuMapper.selectByUserId(userId);
        
        // 转换为 DTO
        List<SysMenuDTO> dtoList = menus.stream().map(menu -> {
            SysMenuDTO dto = new SysMenuDTO();
            BeanUtils.copyProperties(menu, dto);
            return dto;
        }).collect(Collectors.toList());
        
        // 构建树形结构
        List<SysMenuDTO> tree = buildMenuTree(dtoList, 0L);
        
        result.setData(tree);
        return result;
    }

    /**
     * 构建菜单树
     */
    private List<SysMenuDTO> buildMenuTree(List<SysMenuDTO> allMenus, Long parentId) {
        List<SysMenuDTO> tree = new ArrayList<>();
        
        for (SysMenuDTO menu : allMenus) {
            if (parentId.equals(menu.getParentId())) {
                // 递归查找子菜单
                List<SysMenuDTO> children = buildMenuTree(allMenus, menu.getId());
                menu.setChildren(children);
                tree.add(menu);
            }
        }
        
        return tree;
    }
}
