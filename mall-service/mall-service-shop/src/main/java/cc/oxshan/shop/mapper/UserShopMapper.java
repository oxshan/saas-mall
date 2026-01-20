package cc.oxshan.shop.mapper;

import cc.oxshan.shop.entity.UserShop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户店铺关联 Mapper
 */
@Mapper
public interface UserShopMapper {

    int insert(UserShop userShop);

    int delete(@Param("userId") Long userId, @Param("shopId") Long shopId);

    int updateDefault(@Param("userId") Long userId, @Param("shopId") Long shopId);

    int clearDefault(@Param("userId") Long userId);

    List<UserShop> selectByUserId(@Param("userId") Long userId);

    UserShop selectByUserIdAndShopId(@Param("userId") Long userId, @Param("shopId") Long shopId);
}
