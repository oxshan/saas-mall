package cc.oxshan.admin.controller;

import cc.oxshan.admin.client.SysRoleServiceClient;
import cc.oxshan.admin.client.SysUserServiceClient;
import cc.oxshan.admin.dto.LoginReqDTO;
import cc.oxshan.admin.dto.LoginRspDTO;
import cc.oxshan.api.user.dto.SysRoleDTO;
import cc.oxshan.api.user.dto.SysUserDTO;
import cc.oxshan.common.core.enums.ErrorCodeEnum;
import cc.oxshan.common.core.result.Result;
import cc.oxshan.common.security.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证 Controller
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SysUserServiceClient userServiceClient;
    private final SysRoleServiceClient roleServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<LoginRspDTO> login(@RequestBody LoginReqDTO req) {
        // 1. 查询用户
        SysUserDTO user = userServiceClient.getByUsername(req.getUsername());
        if (user == null) {
            return Result.fail(ErrorCodeEnum.AUTH_FAILED);
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return Result.fail(ErrorCodeEnum.AUTH_FAILED);
        }

        // 3. 检查状态
        if (user.getStatus() != 1) {
            return Result.fail(ErrorCodeEnum.ACCOUNT_DISABLED);
        }

        // 4. 获取角色类型
        List<SysRoleDTO> roles = roleServiceClient.getRolesByUserId(user.getId());
        Integer roleType = roles.isEmpty() ? 2 : roles.get(0).getType();

        // 5. 生成 Token
        String token = jwtUtils.generateToken(
            user.getId(), user.getShopId(), user.getUsername(), roleType);

        // 6. 返回结果
        LoginRspDTO rsp = new LoginRspDTO();
        rsp.setToken(token);
        rsp.setUserId(user.getId());
        rsp.setShopId(user.getShopId());
        rsp.setUsername(user.getUsername());
        rsp.setNickname(user.getNickname());
        return Result.ok(rsp);
    }
}
