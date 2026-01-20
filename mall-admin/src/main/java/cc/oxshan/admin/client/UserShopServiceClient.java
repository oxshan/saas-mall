package cc.oxshan.admin.client;

import cc.oxshan.api.shop.UserShopService;
import cc.oxshan.api.shop.dto.ShopDTO;
import cc.oxshan.common.core.exception.BizException;
import cc.oxshan.common.core.result.rpc.PlainResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户店铺关联服务 Client (拆包层)
 */
@Component
public class UserShopServiceClient {

    @DubboReference
    private UserShopService userShopService;

    /**
     * 获取用户的店铺列表
     */
    public List<ShopDTO> getMyShops(Long userId) {
        PlainResult<List<ShopDTO>> result = userShopService.getMyShops(userId);
        checkResult(result);
        return result.getData();
    }

    /**
     * 切换店铺
     */
    public ShopDTO switchShop(Long userId, Long shopId) {
        PlainResult<ShopDTO> result = userShopService.switchShop(userId, shopId);
        checkResult(result);
        return result.getData();
    }

    /**
     * 设置默认店铺
     */
    public void setDefaultShop(Long userId, Long shopId) {
        PlainResult<Void> result = userShopService.setDefaultShop(userId, shopId);
        checkResult(result);
    }

    /**
     * 分配用户到店铺
     */
    public void assignUserToShop(Long userId, Long shopId, Integer isDefault) {
        PlainResult<Void> result = userShopService.assignUserToShop(userId, shopId, isDefault);
        checkResult(result);
    }

    /**
     * 移除用户店铺权限
     */
    public void removeUserFromShop(Long userId, Long shopId) {
        PlainResult<Void> result = userShopService.removeUserFromShop(userId, shopId);
        checkResult(result);
    }

    private void checkResult(PlainResult<?> result) {
        if (result == null || !result.getSuccess()) {
            String msg = result != null ? result.getMsg() : "RPC调用失败";
            int code = result != null ? result.getCode() : 500;
            throw new BizException(code, msg);
        }
    }
}
