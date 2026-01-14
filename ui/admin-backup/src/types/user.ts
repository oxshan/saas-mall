// 用户相关类型
export interface LoginReq {
  username: string
  password: string
}

export interface LoginRsp {
  token: string
  userId: number
  shopId: number
  username: string
  nickname: string
}

export interface UserInfo {
  id: number
  shopId: number
  username: string
  nickname: string
  phone?: string
  email?: string
  avatar?: string
  status: number
}
