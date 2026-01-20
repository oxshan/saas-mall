package cc.oxshan.shop.service.impl;

import cc.oxshan.api.shop.UserShopService;
import cc.oxshan.api.shop.dto.ShopDTO;
import cc.oxshan.common.core.result.rpc.PlainResult;
import cc.oxshan.shop.entity.Shop;
import cc.oxshan.shop.inner.UserShopInnerService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户店铺关联服务实现 (Dubbo)
 */
@DubboService
@RequiredArgsConstructor
public class UserShopServiceImpl implements UserShopService {

    private final UserShopInnerService userShopInnerService;

    @Override
    public PlainResult<List<ShopDTO>> getMyShops(Long userId) {
        PlainResult<List<ShopDTO>> result = new PlainResult<>();
        
        List<Shop> shops = userShopInnerService.getMyShops(userId);
        List<ShopDTO> dtoList = shops.stream().map(shop -> {
            ShopDTO dto = new ShopDTO();
            BeanUtils.copyProperties(shop, dto);
            return dto;
        }).collect(Collectors.toList());
        
        result.setData(dtoList);
        return result;
    }

    @Override
    public PlainResult<ShopDTO> switchShop(Long userId, Long shopId) {
        PlainResult<ShopDTO> result = new PlainResult<>();
        
        Shop shop = userShopInnerService.switchShop(userId, shopId);
        if (shop != null) {
            ShopDTO dto = new ShopDTO();
            BeanUtils.copyProperties(shop, dto);
            result.setData(dto);
        }
        
        return result;
    }

    @Override
    public PlainResult<Void> setDefaultShop(Long userId, Long shopId) {
        PlainResult<Void> result = new PlainResult<>();
        userShopInnerService.setDefault(userId, shopId);
        return result;
    }

    @Override
    public PlainResult<Void> assignUserToShop(Long userId, Long shopId, Integer isDefault) {
        PlainResult<Void> result = new PlainResult<>();
        userShopInnerService.assign(userId, shopId, isDefault);
        return result;
    }

    @Override
    public PlainResult<Void> removeUserFromShop(Long userId, Long shopId) {
        PlainResult<Void> result = new PlainResult<>();
        userShopInnerService.remove(userId, shopId);
        return result;
    }
}
