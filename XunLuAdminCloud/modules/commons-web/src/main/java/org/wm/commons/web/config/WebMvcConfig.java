package org.wm.commons.web.config;

import lombok.RequiredArgsConstructor;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;


/**
 * 功能描述：<功能描述>
 *     配置拦截器
 *
 *
 * @author dove
 * @date 2023/07/14 00:45
 * @since 1.0
 **/
@RequiredArgsConstructor
// @Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    /**
     * 不需要拦截地址
     */
    // public static final String[] excludeUrls = {"/login", "/logout", "/refresh"};

    /*@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getHeaderInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludeUrls)
                .order(-10);
    }*/

    /**
     * 自定义请求头拦截器
     */
    /*public HeaderInterceptor getHeaderInterceptor() {
        return new HeaderInterceptor();
    }*/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        /*registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/").addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(false)
                .addResolver(new WebJarsResourceResolver())
                .addResolver(new PathResourceResolver());*/
    }
}
