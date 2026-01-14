package cc.oxshan.user.service.impl;

import cc.oxshan.api.user.SysUserService;
import cc.oxshan.api.user.dto.SysUserDTO;
import cc.oxshan.common.core.result.rpc.PlainResult;
import cc.oxshan.user.entity.SysRole;
import cc.oxshan.user.entity.SysUser;
import cc.oxshan.user.mapper.SysRoleMapper;
import cc.oxshan.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Set;

/**
 * 用户服务实现 (Dubbo)
 */
@DubboService
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;

    @Override
    public PlainResult<SysUserDTO> getByUsername(String username) {
        PlainResult<SysUserDTO> result = new PlainResult<>();
        SysUser user = userMapper.selectByUsername(username);
        if (user != null) {
            SysUserDTO dto = new SysUserDTO();
            BeanUtils.copyProperties(user, dto);
            result.setData(dto);
        }
        return result;
    }

    @Override
    public PlainResult<Set<String>> getPermissions(Long userId) {
        PlainResult<Set<String>> result = new PlainResult<>();
        result.setData(userMapper.selectPermsByUserId(userId));
        return result;
    }

    @Override
    public PlainResult<Boolean> isSuperAdmin(Long userId) {
        PlainResult<Boolean> result = new PlainResult<>();
        List<SysRole> roles = roleMapper.selectRolesByUserId(userId);
        boolean isSuper = roles.stream().anyMatch(r -> r.getType() == 0);
        result.setData(isSuper);
        return result;
    }
}
