/**
 * 评论功能的组合式函数
 * 提供评论相关的通用逻辑和工具方法
 */
import { ref, computed, type Ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  fetchCommentsByPostId,
  submitCommentToPost,
  deleteComment,
} from "@/api/index.js";
import type { Comment } from "@/api/types/comment";

interface UseCommentsOptions {
  postId: string | number;
  authorId: string | number;
  currentUserId: string | number;
}

export function useComments(options: UseCommentsOptions) {
  const { postId, authorId, currentUserId } = options;

  // 状态管理
  const comments = ref<Comment[]>([]);
  const loading = ref(false);
  const isOpen = ref(true);
  const expandedComments = ref<Record<string, boolean>>({});

  /**
   * 计算总评论数（包括所有回复）
   */
  const totalComments = computed(() => {
    let count = comments.value.length;
    comments.value.forEach((comment) => {
      count += countAllReplies(comment);
    });
    return count;
  });

  /**
   * 递归计算评论的所有回复数量
   */
  const countAllReplies = (comment: Comment): number => {
    if (!comment.voList || comment.voList.length === 0) return 0;
    let count = comment.voList.length;
    comment.voList.forEach((reply) => {
      count += countAllReplies(reply);
    });
    return count;
  };

  /**
   * 计算单个评论的回复数
   */
  const countReplies = (comment: Comment): number => {
    return countAllReplies(comment);
  };

  /**
   * 平铺回复列表（将嵌套结构展平）
   */
  const flattenReplies = (replies: Comment[]): Comment[] => {
    const result: Comment[] = [];
    const flatten = (list: Comment[]) => {
      list.forEach((reply) => {
        result.push(reply);
        if (reply.voList && reply.voList.length > 0) {
          flatten(reply.voList);
        }
      });
    };
    flatten(replies);
    return result;
  };

  /**
   * 在评论列表中查找指定评论
   */
  const findComment = (
    commentList: Comment[],
    commentId: string | number
  ): Comment | null => {
    for (const comment of commentList) {
      if (comment.commentId === commentId) {
        return comment;
      }
      if (comment.voList && comment.voList.length > 0) {
        const found = findComment(comment.voList, commentId);
        if (found) return found;
      }
    }
    return null;
  };

  /**
   * 从评论列表中递归移除评论
   */
  const removeCommentFromList = (
    commentList: Comment[],
    commentId: string | number
  ): boolean => {
    for (let i = 0; i < commentList.length; i++) {
      if (commentList[i].commentId === commentId) {
        commentList.splice(i, 1);
        return true;
      }

      if (commentList[i].voList && commentList[i].voList.length > 0) {
        const removed = removeCommentFromList(commentList[i].voList, commentId);
        if (removed) return true;
      }
    }
    return false;
  };

  /**
   * 检查用户是否有权限删除评论
   */
  const canDeleteComment = (comment: Comment): boolean => {
    // 用户可删除自己的评论
    const isOwnComment = currentUserId === comment.userId;
    // 文章作者可删除自己文章下的所有评论
    const isArticleAuthor = currentUserId === authorId;
    return isOwnComment || isArticleAuthor;
  };

  /**
   * 切换回复展开/收起状态
   */
  const toggleReplies = (commentId: string | number) => {
    expandedComments.value[commentId] = !expandedComments.value[commentId];
  };

  /**
   * 初始化展开状态
   */
  const initExpandedState = () => {
    comments.value.forEach((comment) => {
      expandedComments.value[comment.commentId] = false;
    });
  };

  /**
   * 加载评论列表
   */
  const loadComments = async () => {
    if (!postId) return;
    if (!isOpen.value) return;

    try {
      loading.value = true;
      const res = await fetchCommentsByPostId(postId);
      comments.value = res.data || [];
      initExpandedState();
    } catch (e: any) {
      if (e.msg === "评论区未开启") {
        isOpen.value = false;
      } else {
        ElMessage.error("加载评论失败: " + (e.msg || "未知错误"));
      }
    } finally {
      loading.value = false;
    }
  };

  /**
   * 提交评论
   */
  const submitComment = async (
    content: string,
    parentId: string | number | null = null,
    firstId: string | number | null = null
  ) => {
    if (!content.trim()) {
      ElMessage.warning("请输入内容");
      return null;
    }

    try {
      const response = await submitCommentToPost(postId, {
        content: content.trim(),
        parentId,
        firstId,
      });
      return response.data;
    } catch (e: any) {
      ElMessage.error("发表失败: " + (e.message || "未知错误"));
      return null;
    }
  };

  /**
   * 删除评论
   */
  const handleDeleteComment = async (commentId: string | number) => {
    try {
      await ElMessageBox.confirm("确定要删除这条评论吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      });

      await deleteComment(commentId);
      ElMessage.success("删除成功");

      // 从评论列表中移除已删除的评论
      removeCommentFromList(comments.value, commentId);
      return true;
    } catch (error: any) {
      if (error === "cancel") {
        return false;
      }
      ElMessage.error("删除失败: " + (error.message || "未知错误"));
      return false;
    }
  };

  /**
   * 添加新评论到列表
   */
  const addCommentToList = (
    newComment: Comment,
    parentId?: string | number
  ) => {
    if (!parentId) {
      // 添加到主评论列表
      comments.value.unshift(newComment);
      expandedComments.value[newComment.commentId] = false;
    } else {
      // 添加到回复列表
      const parentComment = findComment(comments.value, parentId);
      if (parentComment) {
        if (!parentComment.voList) {
          parentComment.voList = [];
        }
        parentComment.voList.unshift(newComment);
        // 自动展开回复列表
        expandedComments.value[parentId] = true;
      }
    }
  };

  return {
    // 状态
    comments,
    loading,
    isOpen,
    expandedComments,
    totalComments,

    // 方法
    loadComments,
    submitComment,
    handleDeleteComment,
    canDeleteComment,
    toggleReplies,
    countReplies,
    flattenReplies,
    findComment,
    addCommentToList,
  };
}
