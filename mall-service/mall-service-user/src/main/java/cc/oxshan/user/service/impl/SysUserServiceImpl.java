package cc.oxshan.user.service.impl;

import cc.oxshan.api.user.SysUserService;
import cc.oxshan.api.user.dto.SysUserDTO;
import cc.oxshan.common.core.result.PageResult;
import cc.oxshan.common.core.result.rpc.PlainResult;
import cc.oxshan.user.entity.SysRole;
import cc.oxshan.user.entity.SysUser;
import cc.oxshan.user.mapper.SysRoleMapper;
import cc.oxshan.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户服务实现 (Dubbo)
 */
@DubboService
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

    @Override
    public PlainResult<PageResult<SysUserDTO>> listUsers(Long shopId, Integer pageNum, Integer pageSize) {
        PlainResult<PageResult<SysUserDTO>> result = new PlainResult<>();
        
        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;
        
        // 查询用户列表
        List<SysUser> users = userMapper.selectPage(shopId, offset, pageSize);
        
        // 查询总数
        Long total = userMapper.countByShopId(shopId);
        
        // 转换为 DTO
        List<SysUserDTO> dtoList = users.stream().map(user -> {
            SysUserDTO dto = new SysUserDTO();
            BeanUtils.copyProperties(user, dto);
            // 不返回密码
            dto.setPassword(null);
            return dto;
        }).collect(Collectors.toList());
        
        // 构造分页结果
        PageResult<SysUserDTO> pageResult = PageResult.of(dtoList, total, pageNum, pageSize);
        result.setData(pageResult);
        
        return result;
    }

    @Override
    public PlainResult<SysUserDTO> getUserById(Long userId) {
        PlainResult<SysUserDTO> result = new PlainResult<>();
        SysUser user = userMapper.selectById(userId);
        if (user != null) {
            SysUserDTO dto = new SysUserDTO();
            BeanUtils.copyProperties(user, dto);
            // 不返回密码
            dto.setPassword(null);
            result.setData(dto);
        }
        return result;
    }

    @Override
    public PlainResult<Long> createUser(SysUserDTO dto) {
        PlainResult<Long> result = new PlainResult<>();
        
        // 转换为实体
        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        
        // 加密密码
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        // 插入用户
        userMapper.insert(user);
        
        result.setData(user.getId());
        return result;
    }

    @Override
    public PlainResult<Void> updateUser(SysUserDTO dto) {
        PlainResult<Void> result = new PlainResult<>();
        
        // 转换为实体
        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        
        // 更新用户（不更新密码）
        userMapper.updateById(user);
        
        return result;
    }

    @Override
    public PlainResult<Void> deleteUser(Long userId) {
        PlainResult<Void> result = new PlainResult<>();
        
        // 逻辑删除
        userMapper.deleteById(userId);
        
        return result;
    }

    @Override
    public PlainResult<Void> resetPassword(Long userId, String newPassword) {
        PlainResult<Void> result = new PlainResult<>();
        
        // 加密新密码
        String encodedPassword = passwordEncoder.encode(newPassword);
        
        // 更新密码
        userMapper.updatePassword(userId, encodedPassword);
        
        return result;
    }

    @Override
    public PlainResult<Void> assignRoles(Long userId, List<Long> roleIds) {
        PlainResult<Void> result = new PlainResult<>();
        
        // 先删除旧的角色关联
        userMapper.deleteUserRoles(userId);
        
        // 插入新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            userMapper.insertUserRoles(userId, roleIds);
        }
        
        return result;
    }
}
