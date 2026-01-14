import request from './request'
import type { LoginReq, LoginRsp } from '@/types/user'

export const login = (data: LoginReq) => {
  return request.post<unknown, LoginRsp>('/auth/login', data)
}

export const logout = () => {
  localStorage.removeItem('token')
  window.location.href = '/login'
}
