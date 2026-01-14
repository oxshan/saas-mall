package cc.oxshan.admin.client;

import cc.oxshan.api.user.SysMenuService;
import cc.oxshan.api.user.dto.SysMenuDTO;
import cc.oxshan.common.core.exception.BizException;
import cc.oxshan.common.core.result.rpc.ListResult;
import cc.oxshan.common.core.result.rpc.PlainResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 菜单服务客户端
 * 封装 SysMenuService 的 Dubbo 调用
 */
@Component
@RequiredArgsConstructor
public class SysMenuServiceClient {

    @DubboReference
    private SysMenuService menuService;

    public List<SysMenuDTO> getMenuTree() {
        ListResult<SysMenuDTO> result = menuService.getMenuTree();
        return result.getData() != null ? result.getData() : Collections.emptyList();
    }

    public SysMenuDTO getMenuById(Long menuId) {
        PlainResult<SysMenuDTO> result = menuService.getMenuById(menuId);
        if (!result.isSuccess()) {
            throw new BizException(result.getCode(), result.getMsg());
        }
        return result.getData();
    }

    public Long createMenu(SysMenuDTO dto) {
        PlainResult<Long> result = menuService.createMenu(dto);
        if (!result.isSuccess()) {
            throw new BizException(result.getCode(), result.getMsg());
        }
        return result.getData();
    }

    public void updateMenu(SysMenuDTO dto) {
        PlainResult<Void> result = menuService.updateMenu(dto);
        if (!result.isSuccess()) {
            throw new BizException(result.getCode(), result.getMsg());
        }
    }

    public void deleteMenu(Long menuId) {
        PlainResult<Void> result = menuService.deleteMenu(menuId);
        if (!result.isSuccess()) {
            throw new BizException(result.getCode(), result.getMsg());
        }
    }

    public List<SysMenuDTO> getMenusByUserId(Long userId) {
        ListResult<SysMenuDTO> result = menuService.getMenusByUserId(userId);
        return result.getData() != null ? result.getData() : Collections.emptyList();
    }
}
