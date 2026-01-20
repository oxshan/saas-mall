package cc.oxshan.admin.client;

import cc.oxshan.api.shop.ShopService;
import cc.oxshan.api.shop.dto.ShopCreateDTO;
import cc.oxshan.api.shop.dto.ShopDTO;
import cc.oxshan.api.shop.dto.ShopRegisterDTO;
import cc.oxshan.common.core.exception.BizException;
import cc.oxshan.common.core.result.PageResult;
import cc.oxshan.common.core.result.rpc.PlainResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 店铺服务 Client (拆包层)
 */
@Component
public class ShopServiceClient {

    @DubboReference
    private ShopService shopService;

    /**
     * 店铺注册（创建单店或连锁总部）
     */
    public Map<String, Object> registerShop(ShopRegisterDTO dto, Long userId) {
        PlainResult<Map<String, Object>> result = shopService.registerShop(dto, userId);
        checkResult(result);
        return result.getData();
    }

    /**
     * 创建店铺（创建分店）
     */
    public Map<String, Object> createShop(ShopCreateDTO dto, Long userId, Long parentShopId) {
        PlainResult<Map<String, Object>> result = shopService.createShop(dto, userId, parentShopId);
        checkResult(result);
        return result.getData();
    }

    /**
     * 分页查询店铺列表
     */
    public PageResult<ShopDTO> listShops(Long parentId, String shopName, 
                                         Integer shopType, Integer status, 
                                         Integer pageNum, Integer pageSize) {
        PlainResult<PageResult<ShopDTO>> result = shopService.listShops(
            parentId, shopName, shopType, status, pageNum, pageSize);
        checkResult(result);
        return result.getData();
    }

    /**
     * 根据ID查询店铺
     */
    public ShopDTO getShopById(Long id) {
        PlainResult<ShopDTO> result = shopService.getShopById(id);
        checkResult(result);
        return result.getData();
    }

    /**
     * 更新店铺
     */
    public void updateShop(ShopDTO dto) {
        PlainResult<Void> result = shopService.updateShop(dto);
        checkResult(result);
    }

    /**
     * 删除店铺
     */
    public void deleteShop(Long id) {
        PlainResult<Void> result = shopService.deleteShop(id);
        checkResult(result);
    }

    /**
     * 查询店铺树形结构
     */
    public List<Map<String, Object>> getShopTree() {
        PlainResult<List<Map<String, Object>>> result = shopService.getShopTree();
        checkResult(result);
        return result.getData();
    }

    private void checkResult(PlainResult<?> result) {
        if (result == null || !result.getSuccess()) {
            String msg = result != null ? result.getMsg() : "RPC调用失败";
            int code = result != null ? result.getCode() : 500;
            throw new BizException(code, msg);
        }
    }
}
