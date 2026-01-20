package cc.oxshan.api.shop;

import cc.oxshan.api.shop.dto.ShopCreateDTO;
import cc.oxshan.api.shop.dto.ShopDTO;
import cc.oxshan.api.shop.dto.ShopRegisterDTO;
import cc.oxshan.common.core.result.PageResult;
import cc.oxshan.common.core.result.rpc.PlainResult;

import java.util.List;
import java.util.Map;

/**
 * 店铺服务接口 (Dubbo)
 */
public interface ShopService {

    /**
     * 店铺注册（创建单店或连锁总部）
     */
    PlainResult<Map<String, Object>> registerShop(ShopRegisterDTO dto, Long userId);

    /**
     * 创建店铺（创建分店）
     */
    PlainResult<Map<String, Object>> createShop(ShopCreateDTO dto, Long userId, Long parentShopId);

    /**
     * 分页查询店铺列表
     */
    PlainResult<PageResult<ShopDTO>> listShops(Long parentId, String shopName, 
                                                Integer shopType, Integer status, 
                                                Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询店铺
     */
    PlainResult<ShopDTO> getShopById(Long id);

    /**
     * 更新店铺
     */
    PlainResult<Void> updateShop(ShopDTO dto);

    /**
     * 删除店铺
     */
    PlainResult<Void> deleteShop(Long id);

    /**
     * 查询店铺树形结构
     */
    PlainResult<List<Map<String, Object>>> getShopTree();
}
