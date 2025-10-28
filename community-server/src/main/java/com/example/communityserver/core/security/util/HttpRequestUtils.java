package com.example.communityserver.core.security.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * HttpServletRequest 工具类
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-27
 **/


public class HttpRequestUtils {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final String UTF_8 = "UTF-8";
    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /**
     * 获取请求的完整URL（包括参数）
     */
    public static String getFullRequestUrl(HttpServletRequest request) {
        String queryString = request.getQueryString();
        return request.getRequestURL().toString() + (queryString == null ? "" : "?" + queryString);
    }

    /**
     * 获取客户端真实IP地址（处理代理情况）
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = null;
        for (String header : IP_HEADERS) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                break;
            }
        }

        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        if (LOCALHOST_IPV6.equals(ip)) {
            ip = LOCALHOST_IPV4;
        }

        // 对于多级代理，取第一个有效IP
        if (ip != null && ip.contains(",")) {
            ip = Arrays.stream(ip.split(","))
                    .map(String::trim)
                    .filter(HttpRequestUtils::isValidIp)
                    .findFirst()
                    .orElse(LOCALHOST_IPV4);
        }

        return ip == null ? LOCALHOST_IPV4 : ip;
    }

    private static boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip);
    }

    /**
     * 获取请求头信息
     */
    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    /**
     * 获取所有请求参数（包括URL和表单参数）
     */
    public static Map<String, String> getAllParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        // 获取URL参数
        request.getParameterMap().forEach((key, values) -> {
            if (values.length > 0) {
                params.put(key, values[0]);
            }
        });

        // 如果是POST请求且Content-Type为application/json，尝试解析请求体
        if ("POST".equalsIgnoreCase(request.getMethod())
                && request.getContentType() != null
                && request.getContentType().contains("application/json")) {
            try {
                String body = getRequestBody(request);
                if (body != null && !body.isEmpty()) {
                    // 这里简单处理，实际应该使用JSON解析库
                    Map<String, Object> jsonParams = parseJsonBody(body);
                    jsonParams.forEach((key, value) ->
                            params.put(key, value != null ? value.toString() : null));
                }
            } catch (IOException e) {
                // 忽略解析错误
            }
        }

        return params;
    }

    /**
     * 获取请求体内容
     */
    public static String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 简单解析JSON请求体（实际项目应使用Jackson/Gson等库）
     */
    private static Map<String, Object> parseJsonBody(String jsonBody) {
        // 这里只是示例，实际应该使用JSON解析库
        Map<String, Object> result = new HashMap<>();
        if (jsonBody == null || jsonBody.isEmpty()) {
            return result;
        }

        // 简单处理JSON字符串，实际项目不要这样做
        jsonBody = jsonBody.replaceAll("[{}\"]", "").trim();
        if (jsonBody.isEmpty()) {
            return result;
        }

        String[] pairs = jsonBody.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                result.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        return result;
    }

    /**
     * 获取客户端设备信息
     */
    public static Map<String, String> getDeviceInfo(HttpServletRequest request) {
        Map<String, String> deviceInfo = new HashMap<>();
        String userAgent = request.getHeader("User-Agent");

        deviceInfo.put("ip", getClientIp(request));
        deviceInfo.put("userAgent", userAgent);
        deviceInfo.put("browser", parseBrowser(userAgent));
        deviceInfo.put("os", parseOperatingSystem(userAgent));
        deviceInfo.put("deviceType", parseDeviceType(userAgent));

        return deviceInfo;
    }

    /**
     * 解析浏览器信息
     */
    private static String parseBrowser(String userAgent) {
        if (userAgent == null) {
            return "Unknown";
        }

        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("msie") || userAgent.contains("trident")) {
            return "Internet Explorer";
        } else if (userAgent.contains("edge")) {
            return "Microsoft Edge";
        } else if (userAgent.contains("chrome")) {
            return "Google Chrome";
        } else if (userAgent.contains("firefox")) {
            return "Mozilla Firefox";
        } else if (userAgent.contains("safari")) {
            return "Apple Safari";
        } else if (userAgent.contains("opera")) {
            return "Opera";
        } else {
            return "Unknown";
        }
    }

    /**
     * 解析操作系统信息
     */
    private static String parseOperatingSystem(String userAgent) {
        if (userAgent == null) {
            return "Unknown";
        }

        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("windows")) {
            return "Windows";
        } else if (userAgent.contains("mac os x") || userAgent.contains("macintosh")) {
            return "Mac OS";
        } else if (userAgent.contains("linux")) {
            return "Linux";
        } else if (userAgent.contains("android")) {
            return "Android";
        } else if (userAgent.contains("iphone") || userAgent.contains("ipad")) {
            return "iOS";
        } else {
            return "Unknown";
        }
    }

    /**
     * 解析设备类型
     */
    private static String parseDeviceType(String userAgent) {
        if (userAgent == null) {
            return "Unknown";
        }

        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("mobile") || userAgent.contains("android") || userAgent.contains("iphone")) {
            return "Mobile";
        } else if (userAgent.contains("tablet") || userAgent.contains("ipad")) {
            return "Tablet";
        } else {
            return "Desktop";
        }
    }

    /**
     * 获取请求的基本信息
     */
    public static Map<String, Object> getRequestInfo(HttpServletRequest request) {
        Map<String, Object> info = new HashMap<>();

        info.put("method", request.getMethod());
        info.put("url", getFullRequestUrl(request));
        info.put("protocol", request.getProtocol());
        info.put("scheme", request.getScheme());
        info.put("serverName", request.getServerName());
        info.put("serverPort", request.getServerPort());
        info.put("contextPath", request.getContextPath());
        info.put("servletPath", request.getServletPath());
        info.put("remoteAddr", request.getRemoteAddr());
        info.put("remoteHost", request.getRemoteHost());
        info.put("remotePort", request.getRemotePort());
        info.put("localAddr", request.getLocalAddr());
        info.put("localPort", request.getLocalPort());
        info.put("contentType", request.getContentType());
        info.put("contentLength", request.getContentLength());
        info.put("locale", request.getLocale());
        info.put("queryString", request.getQueryString());

        return info;
    }

    /**
     * 获取所有Cookie
     */
    public static Map<String, String> getCookies(HttpServletRequest request) {
        Map<String, String> cookies = new HashMap<>();
        if (request.getCookies() != null) {
            for (javax.servlet.http.Cookie cookie : request.getCookies()) {
                cookies.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookies;
    }

    /**
     * 获取解码后的请求参数（处理URL编码）
     */
    public static Map<String, String> getDecodedParams(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> decodedParams = new HashMap<>();

        parameterMap.forEach((key, values) -> {
            try {
                String decodedKey = URLDecoder.decode(key, UTF_8);
                String decodedValue = values.length > 0 ? URLDecoder.decode(values[0], UTF_8) : "";
                decodedParams.put(decodedKey, decodedValue);
            } catch (UnsupportedEncodingException e) {
                // 使用默认值
                decodedParams.put(key, values.length > 0 ? values[0] : "");
            }
        });

        return decodedParams;
    }
}
