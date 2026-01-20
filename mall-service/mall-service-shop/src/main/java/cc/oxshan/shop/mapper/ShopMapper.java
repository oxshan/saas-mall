package cc.oxshan.shop.mapper;

import cc.oxshan.shop.entity.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺 Mapper
 */
@Mapper
public interface ShopMapper {

    int insert(Shop shop);

    int updateById(Shop shop);

    int deleteById(@Param("id") Long id);

    Shop selectById(@Param("id") Long id);

    Shop selectByCode(@Param("shopCode") String shopCode);

    List<Shop> selectList(@Param("parentId") Long parentId,
                         @Param("shopName") String shopName,
                         @Param("shopType") Integer shopType,
                         @Param("status") Integer status);

    List<Shop> selectByParentId(@Param("parentId") Long parentId);

    int countBranches(@Param("parentId") Long parentId);
}
