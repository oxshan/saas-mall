package cc.oxshan.shop.inner.impl;

import cc.oxshan.shop.entity.Shop;
import cc.oxshan.shop.entity.UserShop;
import cc.oxshan.shop.inner.UserShopInnerService;
import cc.oxshan.shop.mapper.ShopMapper;
import cc.oxshan.shop.mapper.UserShopMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户店铺关联内部服务实现
 */
@Service
public class UserShopInnerServiceImpl implements UserShopInnerService {

    @Resource
    private UserShopMapper userShopMapper;

    @Resource
    private ShopMapper shopMapper;

    @Override
    public List<Shop> getMyShops(Long userId) {
        List<UserShop> userShops = userShopMapper.selectByUserId(userId);
        List<Shop> shops = new ArrayList<>();
        for (UserShop userShop : userShops) {
            Shop shop = shopMapper.selectById(userShop.getShopId());
            if (shop != null) {
                shops.add(shop);
            }
        }
        return shops;
    }

    @Override
    public Shop switchShop(Long userId, Long shopId) {
        UserShop userShop = userShopMapper.selectByUserIdAndShopId(userId, shopId);
        if (userShop == null) {
            throw new RuntimeException("无权限访问该店铺");
        }
        return shopMapper.selectById(shopId);
    }

    @Override
    @Transactional
    public void setDefault(Long userId, Long shopId) {
        userShopMapper.clearDefault(userId);
        userShopMapper.updateDefault(userId, shopId);
    }

    @Override
    public void assign(Long userId, Long shopId, Integer isDefault) {
        UserShop userShop = new UserShop();
        userShop.setUserId(userId);
        userShop.setShopId(shopId);
        userShop.setIsDefault(isDefault != null ? isDefault : 0);
        userShopMapper.insert(userShop);
    }

    @Override
    public void remove(Long userId, Long shopId) {
        userShopMapper.delete(userId, shopId);
    }
}
