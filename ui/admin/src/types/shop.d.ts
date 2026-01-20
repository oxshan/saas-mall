/**
 * 店铺管理相关类型定义
 */

declare namespace API {
  // ============ 店铺管理 ============
  
  /** 店铺信息 */
  type Shop = {
    id?: number;
    parentId?: number;
    shopCode?: string;
    shopName: string;
    shopType: number;
    province?: string;
    city?: string;
    district?: string;
    address?: string;
    longitude?: number;
    latitude?: number;
    contactName?: string;
    contactPhone?: string;
    businessHours?: string;
    logo?: string;
    images?: string;
    description?: string;
    status: number;
    createdAt?: string;
    updatedAt?: string;
  };

  /** 店铺注册请求 */
  type ShopRegisterReq = {
    shopName: string;
    shopType: number;
    province?: string;
    city?: string;
    district?: string;
    address?: string;
    contactName?: string;
    contactPhone?: string;
    businessHours?: string;
    logo?: string;
    description?: string;
  };

  /** 店铺创建请求 */
  type ShopCreateReq = {
    parentId?: number;
    shopName: string;
    shopType: number;
    province?: string;
    city?: string;
    district?: string;
    address?: string;
    longitude?: number;
    latitude?: number;
    contactName?: string;
    contactPhone?: string;
    businessHours?: string;
    logo?: string;
    description?: string;
  };

  /** 切换店铺请求 */
  type SwitchShopReq = {
    shopId: number;
  };

  /** 分配用户到店铺请求 */
  type AssignUserToShopReq = {
    userId: number;
    shopId: number;
    isDefault?: number;
  };
}
