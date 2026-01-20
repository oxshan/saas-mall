package cc.oxshan.shop.service.impl;

import cc.oxshan.api.shop.ShopService;
import cc.oxshan.api.shop.dto.ShopCreateDTO;
import cc.oxshan.api.shop.dto.ShopDTO;
import cc.oxshan.api.shop.dto.ShopRegisterDTO;
import cc.oxshan.common.core.result.PageResult;
import cc.oxshan.common.core.result.rpc.PlainResult;
import cc.oxshan.shop.entity.Shop;
import cc.oxshan.shop.entity.UserShop;
import cc.oxshan.shop.inner.CodeGeneratorService;
import cc.oxshan.shop.inner.ShopInnerService;
import cc.oxshan.shop.mapper.UserShopMapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 店铺服务实现 (Dubbo)
 */
@DubboService
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopInnerService shopInnerService;
    private final CodeGeneratorService codeGeneratorService;
    private final UserShopMapper userShopMapper;

    @Override
    @Transactional
    public PlainResult<Map<String, Object>> registerShop(ShopRegisterDTO dto, Long userId) {
        PlainResult<Map<String, Object>> result = new PlainResult<>();
        
        Shop shop = new Shop();
        shop.setShopCode(codeGeneratorService.generateShopCode());
        shop.setShopName(dto.getShopName());
        shop.setShopType(dto.getShopType());
        shop.setParentId(0L);
        shop.setProvince(dto.getProvince());
        shop.setCity(dto.getCity());
        shop.setDistrict(dto.getDistrict());
        shop.setAddress(dto.getAddress());
        shop.setContactName(dto.getContactName());
        shop.setContactPhone(dto.getContactPhone());
        shop.setBusinessHours(dto.getBusinessHours());
        shop.setLogo(dto.getLogo());
        shop.setDescription(dto.getDescription());
        shop.setStatus(1);
        shop.setSort(0);
        shop.setCreatedBy(userId);
        shopInnerService.create(shop);

        UserShop userShop = new UserShop();
        userShop.setUserId(userId);
        userShop.setShopId(shop.getId());
        userShop.setIsDefault(1);
        userShopMapper.insert(userShop);

        Map<String, Object> data = new HashMap<>();
        data.put("shopId", shop.getId());
        data.put("shopCode", shop.getShopCode());
        result.setData(data);
        
        return result;
    }

    @Override
    @Transactional
    public PlainResult<Map<String, Object>> createShop(ShopCreateDTO dto, Long userId, Long parentShopId) {
        PlainResult<Map<String, Object>> result = new PlainResult<>();
        
        Shop shop = new Shop();
        shop.setParentId(parentShopId);
        shop.setShopCode(codeGeneratorService.generateShopCode());
        shop.setShopName(dto.getShopName());
        shop.setShopType(dto.getShopType());
        shop.setProvince(dto.getProvince());
        shop.setCity(dto.getCity());
        shop.setDistrict(dto.getDistrict());
        shop.setAddress(dto.getAddress());
        shop.setLongitude(dto.getLongitude());
        shop.setLatitude(dto.getLatitude());
        shop.setContactName(dto.getContactName());
        shop.setContactPhone(dto.getContactPhone());
        shop.setBusinessHours(dto.getBusinessHours());
        shop.setLogo(dto.getLogo());
        shop.setDescription(dto.getDescription());
        shop.setStatus(1);
        shop.setSort(0);
        shop.setCreatedBy(userId);
        shopInnerService.create(shop);

        // 自动关联创建人到店铺
        UserShop userShop = new UserShop();
        userShop.setUserId(userId);
        userShop.setShopId(shop.getId());
        userShop.setIsDefault(0);
        userShopMapper.insert(userShop);

        Map<String, Object> data = new HashMap<>();
        data.put("shopId", shop.getId());
        data.put("shopCode", shop.getShopCode());
        result.setData(data);
        
        return result;
    }

    @Override
    public PlainResult<PageResult<ShopDTO>> listShops(Long parentId, String shopName, 
                                                       Integer shopType, Integer status, 
                                                       Integer pageNum, Integer pageSize) {
        PlainResult<PageResult<ShopDTO>> result = new PlainResult<>();
        
        List<Shop> shops = shopInnerService.list(parentId, shopName, shopType, status);
        
        List<ShopDTO> dtoList = shops.stream().map(shop -> {
            ShopDTO dto = new ShopDTO();
            BeanUtils.copyProperties(shop, dto);
            return dto;
        }).collect(Collectors.toList());
        
        PageResult<ShopDTO> pageResult = PageResult.of(dtoList, (long) dtoList.size(), pageNum, pageSize);
        result.setData(pageResult);
        
        return result;
    }

    @Override
    public PlainResult<ShopDTO> getShopById(Long id) {
        PlainResult<ShopDTO> result = new PlainResult<>();
        
        Shop shop = shopInnerService.getById(id);
        if (shop != null) {
            ShopDTO dto = new ShopDTO();
            BeanUtils.copyProperties(shop, dto);
            result.setData(dto);
        }
        
        return result;
    }

    @Override
    public PlainResult<Void> updateShop(ShopDTO dto) {
        PlainResult<Void> result = new PlainResult<>();
        
        Shop shop = new Shop();
        BeanUtils.copyProperties(dto, shop);
        shopInnerService.update(shop);
        
        return result;
    }

    @Override
    public PlainResult<Void> deleteShop(Long id) {
        PlainResult<Void> result = new PlainResult<>();
        
        shopInnerService.delete(id);
        
        return result;
    }

    @Override
    public PlainResult<List<Map<String, Object>>> getShopTree() {
        PlainResult<List<Map<String, Object>>> result = new PlainResult<>();
        
        List<Map<String, Object>> tree = shopInnerService.tree();
        result.setData(tree);
        
        return result;
    }
}

