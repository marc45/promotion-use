package com.edu.sky.promotion.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 解决网络访问跨域问题
 */
@WebFilter("/*")
@Component
public class CrosFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 此处不进行option请求验证，因为提供页面请求，对于简单请求和非简单请求都要提供支持
        HttpServletResponse crosResponse = (HttpServletResponse) response;
        crosResponse.setHeader("Access-Control-Allow-Origin", "*");
        crosResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        crosResponse.setHeader("Access-Control-Max-Age", "3600");
        crosResponse.setHeader("Access-Control-Allow-Headers", "DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,accept,Content-Type");
        crosResponse.setHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }

}
