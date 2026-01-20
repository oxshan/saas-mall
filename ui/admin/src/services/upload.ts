import { request } from '@umijs/max';

/**
 * 获取七牛云上传 Token
 */
export async function getUploadToken() {
  return request<API.Result<{ token: string; domain: string }>>('/upload/token', {
    method: 'GET',
  });
}
