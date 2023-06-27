package com.gpex.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class SpringUtil {


    private static ApplicationContext applicationContext;

    private static ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    @Autowired
    private void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        SpringUtil.applicationEventPublisher = applicationEventPublisher;
    }


    public static HttpServletRequest getRequest() {
        HttpServletRequest request = null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null) {
            request = requestAttributes.getRequest();
        }
        return request;
    }


    public static HttpServletResponse getResponse() {
        HttpServletResponse response = null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null) {
            response = requestAttributes.getResponse();
        }
        return response;
    }


    public static HttpSession getSession() {
        HttpServletRequest request = SpringUtil.getRequest();
        HttpSession session = null;
        if(request != null) {
            session = request.getSession();
        }
        return session;
    }


    public static WebApplicationContext getWebContext() {
        return ContextLoaderListener.getCurrentWebApplicationContext();
    }


    public static ApplicationContext getApplicationContext() {
        return SpringUtil.applicationContext;
    }


    public static Object getBean(String beanName) {
        Object bean = null;
        WebApplicationContext webApplicationContext = SpringUtil.getWebContext();
        if(webApplicationContext != null) {
            bean = webApplicationContext.getBean(beanName);
        }
        return bean;
    }


    public static <T> T getBean(Class<T> clazz) {
        T bean = null;
        WebApplicationContext webApplicationContext = SpringUtil.getWebContext();
        if(webApplicationContext != null) {
            bean = webApplicationContext.getBean(clazz);
        }
        return bean;
    }

    public static String getContextPath(HttpServletRequest request) {
        return new UrlPathHelper().getContextPath(request);
    }


    public static String getContextPath() {
        HttpServletRequest request = SpringUtil.getRequest();
        return new UrlPathHelper().getContextPath(request);
    }


    public static String getServletPath(HttpServletRequest request) {
        return new UrlPathHelper().getServletPath(request);
    }


    public static String getServletPath() {
        HttpServletRequest request = SpringUtil.getRequest();
        return SpringUtil.getServletPath(request);
    }


    public static String getOriginatingServletPath(HttpServletRequest request) {
        return new UrlPathHelper().getOriginatingServletPath(request);
    }


    public static String getOriginatingServletPath() {
        HttpServletRequest request = SpringUtil.getRequest();
        return new UrlPathHelper().getOriginatingServletPath(request);
    }


    public static String getRequestUri(HttpServletRequest request) {
        return new UrlPathHelper().getRequestUri(request);
    }


    public static String getRequestUri() {
        HttpServletRequest request = SpringUtil.getRequest();
        return new UrlPathHelper().getRequestUri(request);
    }


    public static String getOriginatingRequestUri(HttpServletRequest request) {
        return new UrlPathHelper().getOriginatingRequestUri(request);
    }


    public static String getOriginatingRequestUri() {
        HttpServletRequest request = SpringUtil.getRequest();
        return new UrlPathHelper().getOriginatingRequestUri(request);
    }


    public static String getOriginatingQueryString(HttpServletRequest request) {
        return new UrlPathHelper().getOriginatingQueryString(request);
    }


    public static String getOriginatingQueryString() {
        HttpServletRequest request = SpringUtil.getRequest();
        return new UrlPathHelper().getOriginatingQueryString(request);
    }


    public static String getRequestURL(HttpServletRequest request) {
        StringBuffer url = new StringBuffer();
        String reqUrl = HttpUtils.getRequestURL(request);
        int pathIdx = reqUrl.indexOf("/", request.getScheme().length() + 3);
        if(pathIdx > -1) {
            url.append(reqUrl.substring(0, pathIdx));
        } else {
            url.append(reqUrl);
        }
        String uri = SpringUtil.getRequestUri(request);
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(uri)) {
            url.append(uri);
        }
        return url.toString();
    }


    public static String getRequestURL() {
        HttpServletRequest request = SpringUtil.getRequest();
        return SpringUtil.getRequestURL(request);
    }


    public static String getOriginatingRequestURL(HttpServletRequest request) {
        StringBuffer url = new StringBuffer();
        String reqUrl = HttpUtils.getRequestURL(request);
        int pathIdx = reqUrl.indexOf("/", request.getScheme().length() + 3);
        if(pathIdx > -1) {
            url.append(reqUrl.substring(0, pathIdx));
        } else {
            url.append(reqUrl);
        }
        String uri = SpringUtil.getOriginatingRequestUri(request);
        if(StringUtils.isNotEmpty(uri)) {
            url.append(uri);
        }
        return url.toString();
    }


    public static String getOriginatingRequestURL() {
        HttpServletRequest request = SpringUtil.getRequest();
        return SpringUtil.getOriginatingRequestURL(request);
    }


    public static String getLookupPathForRequest(HttpServletRequest request) {
        return new UrlPathHelper().getLookupPathForRequest(request);
    }


    public static String getLookupPathForRequest() {
        HttpServletRequest request = SpringUtil.getRequest();
        return SpringUtil.getLookupPathForRequest(request);
    }


    public static LocaleResolver getLocalResolver(HttpServletRequest request) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        return localeResolver;
    }


    public static LocaleResolver getLocalResolver() {
        LocaleResolver localeResolver = SpringUtil.getLocalResolver(SpringUtil.getRequest());
        return localeResolver;
    }


    public static Locale getLocale(HttpServletRequest request) {
        LocaleResolver localeResolver = SpringUtil.getLocalResolver();
        Locale locale = localeResolver.resolveLocale(request);
        if(locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }


    public static Locale getLocale() {
        return SpringUtil.getLocale(SpringUtil.getRequest());
    }


    public static void publishEvent(ApplicationEvent event) {
        SpringUtil.applicationEventPublisher.publishEvent(event);
    }


    public static void publishEvent(Object data) {
        SpringUtil.applicationEventPublisher.publishEvent(data);
    }


}
