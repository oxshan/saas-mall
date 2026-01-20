import { request } from '@umijs/max';

/**
 * 获取我的店铺列表
 */
export async function getMyShops() {
  return request<API.Result<API.Shop[]>>('/user-shop/my-shops', {
    method: 'GET',
  });
}

/**
 * 切换店铺
 */
export async function switchShop(data: API.SwitchShopReq) {
  return request<API.Result<API.Shop>>('/user-shop/switch', {
    method: 'POST',
    data,
  });
}

/**
 * 设置默认店铺
 */
export async function setDefaultShop(data: API.SwitchShopReq) {
  return request<API.Result<void>>('/user-shop/set-default', {
    method: 'POST',
    data,
  });
}

/**
 * 分配用户到店铺
 */
export async function assignUserToShop(data: API.AssignUserToShopReq) {
  return request<API.Result<void>>('/user-shop/assign', {
    method: 'POST',
    data,
  });
}

/**
 * 移除用户店铺权限
 */
export async function removeUserFromShop(data: { userId: number; shopId: number }) {
  return request<API.Result<void>>('/user-shop/remove', {
    method: 'DELETE',
    data,
  });
}
