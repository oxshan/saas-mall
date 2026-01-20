package cc.oxshan.shop.inner;

/**
 * 编码生成服务
 */
public interface CodeGeneratorService {

    /**
     * 生成租户编码
     */
    String generateTenantCode();

    /**
     * 生成店铺编码
     */
    String generateShopCode();
}
