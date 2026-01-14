package cc.oxshan.user.service.impl;

import cc.oxshan.api.user.SysRoleService;
import cc.oxshan.api.user.dto.SysRoleDTO;
import cc.oxshan.common.core.exception.BizException;
import cc.oxshan.common.core.result.PageResult;
import cc.oxshan.common.core.result.rpc.ListResult;
import cc.oxshan.common.core.result.rpc.PlainResult;
import cc.oxshan.user.entity.SysRole;
import cc.oxshan.user.mapper.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现 (Dubbo)
 */
@DubboService
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper roleMapper;

    @Override
    public ListResult<SysRoleDTO> getRolesByUserId(Long userId) {
        ListResult<SysRoleDTO> result = new ListResult<>();
        List<SysRole> roles = roleMapper.selectRolesByUserId(userId);
        List<SysRoleDTO> dtoList = roles.stream().map(role -> {
            SysRoleDTO dto = new SysRoleDTO();
            BeanUtils.copyProperties(role, dto);
            return dto;
        }).collect(Collectors.toList());
        result.setData(dtoList);
        return result;
    }

    @Override
    public PlainResult<PageResult<SysRoleDTO>> listRoles(Long shopId, Integer pageNum, Integer pageSize) {
        PlainResult<PageResult<SysRoleDTO>> result = new PlainResult<>();
        
        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;
        
        // 查询角色列表
        List<SysRole> roles = roleMapper.selectPage(shopId, offset, pageSize);
        
        // 查询总数
        Long total = roleMapper.countByShopId(shopId);
        
        // 转换为 DTO
        List<SysRoleDTO> dtoList = roles.stream().map(role -> {
            SysRoleDTO dto = new SysRoleDTO();
            BeanUtils.copyProperties(role, dto);
            return dto;
        }).collect(Collectors.toList());
        
        // 构造分页结果
        PageResult<SysRoleDTO> pageResult = PageResult.of(dtoList, total, pageNum, pageSize);
        result.setData(pageResult);
        
        return result;
    }

    @Override
    public PlainResult<SysRoleDTO> getRoleById(Long roleId) {
        PlainResult<SysRoleDTO> result = new PlainResult<>();
        SysRole role = roleMapper.selectById(roleId);
        if (role != null) {
            SysRoleDTO dto = new SysRoleDTO();
            BeanUtils.copyProperties(role, dto);
            result.setData(dto);
        }
        return result;
    }

    @Override
    public PlainResult<Long> createRole(SysRoleDTO dto) {
        PlainResult<Long> result = new PlainResult<>();
        
        // 转换为实体
        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto, role);
        
        // 插入角色
        roleMapper.insert(role);
        
        result.setData(role.getId());
        return result;
    }

    @Override
    public PlainResult<Void> updateRole(SysRoleDTO dto) {
        PlainResult<Void> result = new PlainResult<>();
        
        // 转换为实体
        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto, role);
        
        // 更新角色
        roleMapper.updateById(role);
        
        return result;
    }

    @Override
    public PlainResult<Void> deleteRole(Long roleId) {
        PlainResult<Void> result = new PlainResult<>();
        
        // 检查角色是否存在
        SysRole role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BizException(1003, "角色不存在");
        }
        
        // 检查是否为系统内置角色
        if (role.getIsSystem() == 1) {
            throw new BizException(500, "系统内置角色不可删除");
        }
        
        // 检查是否有用户使用该角色
        Long userCount = roleMapper.countUsersByRoleId(roleId);
        if (userCount > 0) {
            throw new BizException(500, "该角色下还有用户，无法删除");
        }
        
        // 删除角色
        roleMapper.deleteById(roleId);
        
        // 删除角色菜单关联
        roleMapper.deleteRoleMenus(roleId);
        
        return result;
    }

    @Override
    public PlainResult<Void> assignMenus(Long roleId, List<Long> menuIds) {
        PlainResult<Void> result = new PlainResult<>();
        
        // 先删除旧的菜单关联
        roleMapper.deleteRoleMenus(roleId);
        
        // 插入新的菜单关联
        if (menuIds != null && !menuIds.isEmpty()) {
            roleMapper.insertRoleMenus(roleId, menuIds);
        }
        
        return result;
    }

    @Override
    public ListResult<Long> getRoleMenuIds(Long roleId) {
        ListResult<Long> result = new ListResult<>();
        List<Long> menuIds = roleMapper.selectMenuIdsByRoleId(roleId);
        result.setData(menuIds);
        return result;
    }
}
