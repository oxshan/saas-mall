// 用户信息
export interface UserInfo {
  id: number;
  shopId: number;
  username: string;
  nickname: string;
  phone?: string;
  email?: string;
  avatar?: string;
  status: number;
  roles: string[];
  permissions: string[];
}

// 登录请求
export interface LoginParams {
  username: string;
  password: string;
}

// 登录响应
export interface LoginResult {
  token: string;
  userInfo: UserInfo;
}
