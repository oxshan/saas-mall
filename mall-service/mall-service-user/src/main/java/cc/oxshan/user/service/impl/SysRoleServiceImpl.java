package cc.oxshan.user.service.impl;

import cc.oxshan.api.user.SysRoleService;
import cc.oxshan.api.user.dto.SysRoleDTO;
import cc.oxshan.common.core.result.rpc.ListResult;
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
}
