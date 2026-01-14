package cc.oxshan.admin.client;

import cc.oxshan.api.user.SysRoleService;
import cc.oxshan.api.user.dto.SysRoleDTO;
import cc.oxshan.common.core.exception.BizException;
import cc.oxshan.common.core.result.PageResult;
import cc.oxshan.common.core.result.rpc.ListResult;
import cc.oxshan.common.core.result.rpc.PlainResult;
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
        checkListResult(result);
        return result.getData() != null ? result.getData() : Collections.emptyList();
    }

    /**
     * 分页查询角色列表
     */
    public PageResult<SysRoleDTO> listRoles(Long shopId, Integer pageNum, Integer pageSize) {
        PlainResult<PageResult<SysRoleDTO>> result = sysRoleService.listRoles(shopId, pageNum, pageSize);
        checkPlainResult(result);
        return result.getData();
    }

    /**
     * 根据ID查询角色
     */
    public SysRoleDTO getRoleById(Long roleId) {
        PlainResult<SysRoleDTO> result = sysRoleService.getRoleById(roleId);
        checkPlainResult(result);
        return result.getData();
    }

    /**
     * 创建角色
     */
    public Long createRole(SysRoleDTO dto) {
        PlainResult<Long> result = sysRoleService.createRole(dto);
        checkPlainResult(result);
        return result.getData();
    }

    /**
     * 更新角色
     */
    public void updateRole(SysRoleDTO dto) {
        PlainResult<Void> result = sysRoleService.updateRole(dto);
        checkPlainResult(result);
    }

    /**
     * 删除角色
     */
    public void deleteRole(Long roleId) {
        PlainResult<Void> result = sysRoleService.deleteRole(roleId);
        checkPlainResult(result);
    }

    /**
     * 分配菜单权限
     */
    public void assignMenus(Long roleId, List<Long> menuIds) {
        PlainResult<Void> result = sysRoleService.assignMenus(roleId, menuIds);
        checkPlainResult(result);
    }

    /**
     * 查询角色的菜单ID列表
     */
    public List<Long> getRoleMenuIds(Long roleId) {
        ListResult<Long> result = sysRoleService.getRoleMenuIds(roleId);
        checkListResult(result);
        return result.getData() != null ? result.getData() : Collections.emptyList();
    }

    private void checkPlainResult(PlainResult<?> result) {
        if (result == null || !result.getSuccess()) {
            String msg = result != null ? result.getMsg() : "RPC调用失败";
            int code = result != null ? result.getCode() : 500;
            throw new BizException(code, msg);
        }
    }

    private void checkListResult(ListResult<?> result) {
        if (result == null || !result.getSuccess()) {
            String msg = result != null ? result.getMsg() : "RPC调用失败";
            int code = result != null ? result.getCode() : 500;
            throw new BizException(code, msg);
        }
    }
}
