/**
 * 评论表单的组合式函数
 * 处理表单状态、验证和提交逻辑
 */
import { ref, computed } from "vue";

interface UseCommentFormOptions {
  maxLength?: number;
  minLength?: number;
  placeholder?: string;
  onSubmit: (content: string) => Promise<any>;
}

export function useCommentForm(options: UseCommentFormOptions) {
  const {
    maxLength = 500,
    minLength = 1,
    placeholder = "写下你的评论...",
    onSubmit,
  } = options;

  const content = ref("");
  const submitting = ref(false);
  const focused = ref(false);

  /**
   * 字数统计
   */
  const charCount = computed(() => content.value.length);

  /**
   * 是否超出字数限制
   */
  const isOverLimit = computed(() => charCount.value > maxLength);

  /**
   * 是否可以提交
   */
  const canSubmit = computed(() => {
    return (
      content.value.trim().length >= minLength &&
      content.value.length <= maxLength &&
      !submitting.value
    );
  });

  /**
   * 字数显示颜色
   */
  const charCountColor = computed(() => {
    const ratio = charCount.value / maxLength;
    if (ratio >= 1) return "#f56c6c";
    if (ratio >= 0.8) return "#e6a23c";
    return "#909399";
  });

  /**
   * 提交表单
   */
  const handleSubmit = async () => {
    if (!canSubmit.value) return;

    try {
      submitting.value = true;
      const result = await onSubmit(content.value.trim());
      if (result !== null) {
        reset();
      }
      return result;
    } finally {
      submitting.value = false;
    }
  };

  /**
   * 重置表单
   */
  const reset = () => {
    content.value = "";
    focused.value = false;
  };

  /**
   * 获取焦点
   */
  const focus = () => {
    focused.value = true;
  };

  /**
   * 失去焦点
   */
  const blur = () => {
    focused.value = false;
  };

  return {
    // 状态
    content,
    submitting,
    focused,
    charCount,
    isOverLimit,
    canSubmit,
    charCountColor,
    placeholder,
    maxLength,

    // 方法
    handleSubmit,
    reset,
    focus,
    blur,
  };
}
