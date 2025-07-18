import router from '@/router';

/**
 * 在新标签页导航
 * @param {RouteLocationRaw} location 路由位置
 * @param {boolean} noReferrer 是否禁止referrer
 */
export const navigateInNewTab = (location, noReferrer = false) => {
    const route = router.resolve(location);
    const url = window.location.origin + route.href;
    const newWindow = window.open(url, '_blank');

    if (noReferrer && newWindow) {
        newWindow.opener = null;
    }

    return newWindow;
}