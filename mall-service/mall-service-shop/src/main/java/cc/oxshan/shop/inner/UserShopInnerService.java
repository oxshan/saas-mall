package cc.oxshan.shop.inner;

import cc.oxshan.shop.entity.Shop;

import java.util.List;

/**
 * 用户店铺关联内部服务
 */
public interface UserShopInnerService {

    /**
     * 获取用户的店铺列表
     */
    List<Shop> getMyShops(Long userId);

    /**
     * 切换店铺
     */
    Shop switchShop(Long userId, Long shopId);

    /**
     * 设置默认店铺
     */
    void setDefault(Long userId, Long shopId);

    /**
     * 分配用户到店铺
     */
    void assign(Long userId, Long shopId, Integer isDefault);

    /**
     * 移除用户店铺权限
     */
    void remove(Long userId, Long shopId);
}
