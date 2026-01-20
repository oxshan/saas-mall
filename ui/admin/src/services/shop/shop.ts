import { request } from '@umijs/max';

/**
 * 店铺注册（创建单店或连锁总部）
 */
export async function registerShop(data: API.ShopRegisterReq) {
  return request<API.Result<any>>('/shop/register', {
    method: 'POST',
    data,
  });
}

/**
 * 创建店铺（创建分店）
 */
export async function createShop(data: API.ShopCreateReq) {
  return request<API.Result<any>>('/shop/add', {
    method: 'POST',
    data,
  });
}

/**
 * 店铺列表（分页）
 */
export async function listShops(params: {
  parentId?: number;
  shopName?: string;
  shopType?: number;
  status?: number;
  pageNum?: number;
  pageSize?: number;
}) {
  return request<API.Result<API.PageResult<API.Shop>>>('/shop/list', {
    method: 'GET',
    params,
  });
}

/**
 * 店铺详情
 */
export async function getShopById(id: number) {
  return request<API.Result<API.Shop>>(`/shop/${id}`, {
    method: 'GET',
  });
}

/**
 * 更新店铺
 */
export async function updateShop(data: API.Shop) {
  return request<API.Result<void>>('/shop/update', {
    method: 'POST',
    data,
  });
}

/**
 * 删除店铺
 */
export async function deleteShop(id: number) {
  return request<API.Result<void>>(`/shop/${id}`, {
    method: 'DELETE',
  });
}

/**
 * 店铺树形结构
 */
export async function getShopTree() {
  return request<API.Result<any[]>>('/shop/tree', {
    method: 'GET',
  });
}
