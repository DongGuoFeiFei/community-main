/**
 * 后端通用返回数据结构：Result<T>
 */
export interface ApiResponse<T> {
  code: number;
  msg: string;
  data: T;
}

/**
 * 后端通用分页返回数据结构：Result<PageData<T>>
 */
export interface PageData<T> {
  total: number;
  rows: T[];
}
