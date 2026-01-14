package cc.oxshan.api.user;

import cc.oxshan.api.user.dto.SysRoleDTO;
import cc.oxshan.common.core.result.rpc.ListResult;

/**
 * 角色服务接口 (Dubbo)
 */
public interface SysRoleService {

    /**
     * 根据用户ID查询角色列表
     */
    ListResult<SysRoleDTO> getRolesByUserId(Long userId);
}
