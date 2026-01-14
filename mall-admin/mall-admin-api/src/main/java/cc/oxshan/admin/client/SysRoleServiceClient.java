package cc.oxshan.admin.client;

import cc.oxshan.api.user.SysRoleService;
import cc.oxshan.api.user.dto.SysRoleDTO;
import cc.oxshan.common.core.exception.BizException;
import cc.oxshan.common.core.result.rpc.ListResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 角色服务 Client (拆包层)
 */
@Component
public class SysRoleServiceClient {

    @DubboReference
    private SysRoleService sysRoleService;

    /**
     * 根据用户ID查询角色列表
     */
    public List<SysRoleDTO> getRolesByUserId(Long userId) {
        ListResult<SysRoleDTO> result = sysRoleService.getRolesByUserId(userId);
        checkResult(result);
        return result.getData() != null ? result.getData() : Collections.emptyList();
    }

    private void checkResult(ListResult<?> result) {
        if (result == null || !result.getSuccess()) {
            String msg = result != null ? result.getMsg() : "RPC调用失败";
            int code = result != null ? result.getCode() : 500;
            throw new BizException(code, msg);
        }
    }
}
