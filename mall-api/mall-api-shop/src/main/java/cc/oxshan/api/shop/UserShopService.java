package cc.oxshan.api.shop;

import cc.oxshan.api.shop.dto.ShopDTO;
import cc.oxshan.common.core.result.rpc.PlainResult;

import java.util.List;

/**
 * 用户店铺关联服务接口 (Dubbo)
 */
public interface UserShopService {

    /**
     * 获取用户的店铺列表
     */
    PlainResult<List<ShopDTO>> getMyShops(Long userId);

    /**
     * 切换店铺
     */
    PlainResult<ShopDTO> switchShop(Long userId, Long shopId);

    /**
     * 设置默认店铺
     */
    PlainResult<Void> setDefaultShop(Long userId, Long shopId);

    /**
     * 分配用户到店铺
     */
    PlainResult<Void> assignUserToShop(Long userId, Long shopId, Integer isDefault);

    /**
     * 移除用户店铺权限
     */
    PlainResult<Void> removeUserFromShop(Long userId, Long shopId);
}
