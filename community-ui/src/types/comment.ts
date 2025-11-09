/**
 * 评论相关的类型定义
 */

export interface Comment {
  commentId: string | number;
  userId: string | number;
  nickname: string;
  accessUrl: string;
  content: string;
  createdAt: string;
  parentId?: string | number | null;
  firstId?: string | number | null;
  repliedNickname?: string;
  voList?: Comment[];
}

export interface CommentFormData {
  content: string;
  parentId?: string | number | null;
  firstId?: string | number | null;
}

export interface CommentSubmitResponse {
  code: number;
  msg: string;
  data: Comment;
}
