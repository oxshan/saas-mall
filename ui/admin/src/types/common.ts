// API 响应类型
export interface Result<T> {
  code: number;
  msg: string;
  data: T;
}

// 分页参数
export interface PageParams {
  current?: number;
  pageSize?: number;
}

// 分页结果
export interface PageResult<T> {
  list: T[];
  total: number;
}
