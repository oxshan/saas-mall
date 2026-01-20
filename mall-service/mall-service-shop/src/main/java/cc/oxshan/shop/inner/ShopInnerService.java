package cc.oxshan.shop.inner;

import cc.oxshan.shop.entity.Shop;

import java.util.List;
import java.util.Map;

/**
 * 店铺内部服务
 */
public interface ShopInnerService {

    /**
     * 创建店铺
     */
    Shop create(Shop shop);

    /**
     * 查询店铺列表
     */
    List<Shop> list(Long parentId, String shopName, Integer shopType, Integer status);

    /**
     * 查询店铺详情
     */
    Shop getById(Long id);

    /**
     * 更新店铺
     */
    void update(Shop shop);

    /**
     * 删除店铺
     */
    void delete(Long id);

    /**
     * 查询店铺树形结构
     */
    List<Map<String, Object>> tree();
}
