package com.gpex.common.utils;

import net.sf.uadetector.UserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
    private static final UserAgentStringParser USER_AGENT_STRING_PARSER = UADetectorServiceFactory.getResourceModuleParser();


    public static String getContextPath(HttpServletRequest request) {
        return request.getContextPath();
    }


    public static String getRequestURL(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    /**
     * return http(s)://xxx.xxx.xx.xx:xx
     * @param request
     * @return
     */
    public static String getRequestHost(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        sb.append(request.isSecure() ? "https://" : "http://")
                .append(request.getServerName()).append(":").append(request.getServerPort());

        return sb.toString();
    }

    public static String getRequestHost() {
        return getRequestHost(SpringUtil.getRequest());
    }


    public static boolean isMultipart(HttpServletRequest request) {
        String contentType = request.getContentType();
        if(contentType != null && contentType.toLowerCase().startsWith("multipart/form-data")) {
            return true;
        }
        return false;
    }


    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(",");
            if(index != -1) {
                ip = ip.substring(0, index);
            }
            if(ip != null) {
                ip = ip.trim();
            }
            return ip;
        }
        ip = request.getHeader("http_client_ip");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("http_x_forwarded");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("http_x_forwarded_for");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("http_x_cluster_client_ip");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("http_forwarded");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("remote_addr");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("proxy-client-ip");  // weblogic
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("wl-proxy-client-ip");  // weblogic
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }


    public static String getRemoteIp() {
        return HttpUtils.getRemoteIp(SpringUtil.getRequest());
    }


    public static int getServerPort(HttpServletRequest request) {
        String forwardedPort = request.getHeader("x-forwarded-port");
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(forwardedPort) && !"unknown".equalsIgnoreCase(forwardedPort)) {
            int index = forwardedPort.indexOf(",");
            if(index != -1) {
                forwardedPort = forwardedPort.substring(0, index);
            }
            if(forwardedPort != null) {
                forwardedPort = forwardedPort.trim();
            }
            return NumberUtils.toInt(forwardedPort);
        }
        return request.getServerPort();
    }

    public static Map<String, Object> getUserAgentMap() {
        return getUserAgentMap(getUserAgent());
    }

    public static Map<String, Object> getUserAgentMap(HttpServletRequest request) {
        return getUserAgentMap(getUserAgent(request));
    }

    public static Map<String, Object> getUserAgentMap(String userAgent) {
        return StringUtils.toMap(getUserAgentObject(userAgent));
    }

    public static UserAgent getUserAgentObject(String userAgent) {
        return (UserAgent) USER_AGENT_STRING_PARSER.parse(userAgent);
    }

    /**
     * Format : {Browser Name} {version} ({OS} {version})
     * EX) Chrome 91.0 (OS X 10.15)
     * @return
     */
    public static String getUserAgentSimple() {
        return getUserAgentSimple(getUserAgent());
    }

    /**
     * Format : {Browser Name} {version} ({OS} {version})
     * EX) Chrome 91.0 (OS X 10.15)
     * @param userAgent
     * @return
     */
    public static String getUserAgentSimple(String userAgent) {
        UserAgent agent = getUserAgentObject(userAgent);

        StringBuilder sb = new StringBuilder();
        sb.append(agent.getName()).append(" ").append(agent.getVersionNumber().getMajor()).append(".").append(agent.getVersionNumber().getMinor());
        sb.append(" (").append(agent.getOperatingSystem().getName()).append(" ").append(agent.getOperatingSystem().getVersionNumber().getMajor()).append(".").append(agent.getOperatingSystem().getVersionNumber().getMinor()).append(")");

        return sb.toString();
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public static String getUserAgent() {
        return SpringUtil.getRequest().getHeader("User-Agent");
    }

    public static String getServerIp() {
        String ip = null;
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            boolean isExit = false;
            while(en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                if(ni == null || !ni.isUp() || ni.isLoopback() || ni.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
                while(inetAddresses.hasMoreElements()) {
                    InetAddress ia = inetAddresses.nextElement();
                    if(ia.isLoopbackAddress() || ia.isLinkLocalAddress()) {
                        continue;
                    }
                    if(ia.isSiteLocalAddress()) {
                        return ia.getHostAddress();
                    }
                    if(ia.getHostAddress() != null && ia.getHostAddress().indexOf(".") != -1) {
                        ip = ia.getHostAddress();
                        isExit = true;
                        break;
                    }
                }
                if(isExit) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("error : {}", e.getMessage());
        }
        return ip;
    }


    public static Map<String, String> getHeaderBox(Header[] headers) {
        Map<String, String> headerBox = null;
        if(headers != null) {
            headerBox = new HashMap<>();
            String key = null, value = null;
            for(Header header : headers) {
                key = header.getName();
                if(org.apache.commons.lang3.StringUtils.isNotEmpty(key)) {
                    value = header.getValue();
                    headerBox.put(key.toLowerCase(), value);
                }
            }
        }
        return headerBox;
    }


    public static Map<String, String>  getHeaderBox(HttpServletRequest request) {
        Map<String, String>  headerBox = null;
        if(request != null) {
            headerBox = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            String key = null, value = null;
            while(headerNames.hasMoreElements()) {
                key = headerNames.nextElement();
                value = request.getHeader(key);
                if(org.apache.commons.lang3.StringUtils.isNotEmpty(value)) {
                    headerBox.put(key.toLowerCase(), value);
                }
            }
        }
        return headerBox;
    }


    public static Map<String, String>  getHeaderBox(HttpServletResponse response) {
        Map<String, String>  headerBox = null;
        if(response != null) {
            headerBox = new HashMap<>();
            Iterator<String> iter = response.getHeaderNames().iterator();
            String key = null, value = null;
            while(iter.hasNext()) {
                key = iter.next();
                value = response.getHeader(key);
                if(org.apache.commons.lang3.StringUtils.isNotEmpty(value)) {
                    headerBox.put(key.toLowerCase(), value);
                }
            }
        }
        return headerBox;
    }


    public static Map<String, Object>  getParamBox(HttpServletRequest request) {
        Map<String, Object>  paramBox = null;
        if(request != null) {
            paramBox = new HashMap<>();
            Enumeration<String> enumeration = request.getParameterNames();
            String key = null;
            String[] values = null;
            while (enumeration.hasMoreElements()) {
                key = enumeration.nextElement();
                values = request.getParameterValues(key);
                if (values != null) {
                    try {
                        if(values.length > 1) {
                            paramBox.put(key, values);
                        } else {
                            paramBox.put(key, values[0]);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return paramBox;
    }


    public static String getBody(HttpServletRequest request, String charset) {
        String reqData = null;
        byte[] reqByte;
        try {
            InputStream in = request.getInputStream();
            reqByte = IOUtils.toByteArray(in);
            if(reqByte.length > 0) {
                reqData = new String(reqByte, charset);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return reqData;
    }


    public static String getBody(HttpServletRequest request) {
        return HttpUtils.getBody(request, "UTF-8");
    }


    public static String getMethod(HttpServletRequest request) {
        return request.getMethod();
    }


    public static String getRefererURL(HttpServletRequest request) {
        return request.getHeader("referer");
    }


    public static String getRefererUri(HttpServletRequest request) {
        String refererUrl = HttpUtils.getRefererURL(request);
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(refererUrl)) {
            int idx = -1;
            String contextPath = HttpUtils.getContextPath(request);
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(contextPath)) {
                idx = refererUrl.indexOf(contextPath);
            } else {
                idx = refererUrl.indexOf("/", request.getScheme().length() + 3);
            }
            if(idx > -1) {
                return refererUrl.substring(idx);
            }
        }
        return null;
    }


    public static String getRefererPath(HttpServletRequest request) {
        String refererUrl = HttpUtils.getRefererURL(request);
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(refererUrl)) {
            int idx = -1;
            String contextPath = HttpUtils.getContextPath(request);
            if(StringUtils.isNotEmpty(contextPath)) {
                idx = refererUrl.indexOf(contextPath);
                if(idx != -1) {
                    idx += contextPath.length();
                }
            } else {
                idx = refererUrl.indexOf("/", request.getScheme().length() + 3);
            }
            if(idx > -1) {
                return refererUrl.substring(idx);
            }
        }
        return null;
    }


    public static boolean isExcelExtension(HttpServletRequest request) {
        String uri = SpringUtil.getOriginatingServletPath(request);
        if(uri.endsWith(".excel")) {
            return true;
        }
        return false;
    }


    public static boolean isFileExtension(HttpServletRequest request) {
        String uri = SpringUtil.getOriginatingServletPath(request);
        if(uri.endsWith(".file")) {
            return true;
        }
        return false;
    }


    public static boolean isReportExtension(HttpServletRequest request) {
        String uri = SpringUtil.getOriginatingServletPath(request);
        if(uri.endsWith(".report")) {
            return true;
        }
        return false;
    }

    public static String getRequestOS() {
        String userAgent = getUserAgent();

        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            if (userAgent.contains("windows")) return "Windows";
            if (userAgent.contains("macintosh")) return "Mac";
            if (userAgent.contains("linux")) return "Linux";
            if (userAgent.contains("android")) return "Android";
            if (userAgent.contains("iphone")) return "iPhone";
            if (userAgent.contains("ipad")) return "iPad";
        }

        return null;
    }

}
