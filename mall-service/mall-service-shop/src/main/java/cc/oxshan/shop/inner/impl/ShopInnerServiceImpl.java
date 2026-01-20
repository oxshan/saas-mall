package cc.oxshan.shop.inner.impl;

import cc.oxshan.shop.entity.Shop;
import cc.oxshan.shop.inner.ShopInnerService;
import cc.oxshan.shop.mapper.ShopMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺内部服务实现
 */
@Service
public class ShopInnerServiceImpl implements ShopInnerService {

    @Resource
    private ShopMapper shopMapper;

    @Override
    public Shop create(Shop shop) {
        shopMapper.insert(shop);
        return shop;
    }

    @Override
    public List<Shop> list(Long parentId, String shopName, Integer shopType, Integer status) {
        return shopMapper.selectList(parentId, shopName, shopType, status);
    }

    @Override
    public Shop getById(Long id) {
        return shopMapper.selectById(id);
    }

    @Override
    public void update(Shop shop) {
        shopMapper.updateById(shop);
    }

    @Override
    public void delete(Long id) {
        int branchCount = shopMapper.countBranches(id);
        if (branchCount > 0) {
            throw new RuntimeException("请先删除所有分店");
        }
        shopMapper.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> tree() {
        List<Shop> headquarters = shopMapper.selectList(0L, null, 2, null);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Shop hq : headquarters) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", hq.getId());
            node.put("shopCode", hq.getShopCode());
            node.put("shopName", hq.getShopName());
            node.put("shopType", hq.getShopType());
            node.put("status", hq.getStatus());
            
            List<Shop> branches = shopMapper.selectByParentId(hq.getId());
            List<Map<String, Object>> children = new ArrayList<>();
            for (Shop branch : branches) {
                Map<String, Object> childNode = new HashMap<>();
                childNode.put("id", branch.getId());
                childNode.put("shopCode", branch.getShopCode());
                childNode.put("shopName", branch.getShopName());
                childNode.put("shopType", branch.getShopType());
                childNode.put("status", branch.getStatus());
                childNode.put("children", new ArrayList<>());
                children.add(childNode);
            }
            node.put("children", children);
            result.add(node);
        }
        
        return result;
    }
}
