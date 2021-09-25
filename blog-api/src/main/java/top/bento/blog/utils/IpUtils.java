package top.bento.blog.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * fetch ip
 *
 */
@Slf4j
public class IpUtils {

    /**
     * fetch ip addr
     * <p>
     * if using nginx, then unable to use request.getRemoteAddr() to get ip addr
     * if using multi reverse proxy service, there might exist several X-Forwarded-For value
     * i.e. series of ip address, then the first valid(not unknown) ip field serves as the actual ip addr
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null, unknown = "unknown", seperator = ",";
        int maxLength = 15;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IpUtils ERROR ", e);
        }

        // if proxy used, get the first ip
        if (StringUtils.isEmpty(ip) && ip.length() > maxLength) {
            int idx = ip.indexOf(seperator);
            if (idx > 0) {
                ip = ip.substring(0, idx);
            }
        }

        return ip;
    }

    /**
     * fetch ip addr
     *
     * @return
     */
    public static String getIpAddr() {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        return getIpAddr(request);
    }
}
