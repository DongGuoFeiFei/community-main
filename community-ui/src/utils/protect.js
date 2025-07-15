export const setupProtection = () => {
    // 1. 禁用右键菜单
    document.addEventListener('contextmenu', (e) => e.preventDefault());

    // 2. 拦截开发者工具快捷键（Ctrl+Shift+I / Ctrl+Shift+C / Ctrl+U）
    document.addEventListener('keydown', (e) => {
        if (
            e.key === 'F12' || // Chrome/Firefox 开发者工具
            (e.ctrlKey && e.shiftKey && e.key === 'I') || // Chrome 开发者工具
            (e.ctrlKey && e.shiftKey && e.key === 'J') || // Chrome 开发者工具-Console
            (e.ctrlKey && e.shiftKey && e.key === 'C') || // Chrome 开发者工具-检查元素
            (e.ctrlKey && e.key === 'U') // 查看源代码
        ) {
            e.preventDefault();
        }
    });
};
