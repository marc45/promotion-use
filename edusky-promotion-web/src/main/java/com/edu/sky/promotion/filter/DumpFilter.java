package com.edu.sky.promotion.filter;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@WebFilter(value = "/*", asyncSupported = true)
@Component
public class DumpFilter implements Filter {

    private static class ByteArrayServletStream extends ServletOutputStream {
        private ByteArrayOutputStream bStream;

        ByteArrayServletStream(ByteArrayOutputStream bStream) {
            this.bStream = bStream;
        }

        public void write(int param) throws IOException {
            bStream.write(param);
        }


        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }

    private static class ByteArrayPrintWriter {
        private ByteArrayOutputStream baos = new ByteArrayOutputStream();
        private PrintWriter pw = new PrintWriter(baos);
        private ServletOutputStream sos = new ByteArrayServletStream(baos);

        public PrintWriter getWriter() {
            return pw;
        }

        public ServletOutputStream getStream() {
            return sos;
        }

        byte[] toByteArray() {
            return baos.toByteArray();
        }
    }

    private class BufferedServletInputStream extends ServletInputStream {
        ByteArrayInputStream bais;

        public BufferedServletInputStream(ByteArrayInputStream bais) {
            this.bais = bais;
        }

        public int available() {
            return bais.available();
        }

        public int read() {
            return bais.read();
        }

        public int read(byte[] buf, int off, int len) {
            return bais.read(buf, off, len);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }
    }

    private class BufferedRequestWrapper extends HttpServletRequestWrapper {
        ByteArrayInputStream bais;
        ByteArrayOutputStream baos;
        BufferedServletInputStream bsis;
        byte[] buffer;

        public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
            super(req);
            InputStream is = req.getInputStream();
            baos = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int letti;
            while ((letti = is.read(buf)) > 0) {
                baos.write(buf, 0, letti);
            }
            buffer = baos.toByteArray();
        }

        public ServletInputStream getInputStream() {
            try {
                bais = new ByteArrayInputStream(buffer);
                bsis = new BufferedServletInputStream(bais);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return bsis;
        }

        public byte[] getBuffer() {
            return buffer;
        }

    }

    private boolean dumpRequest;
    private boolean dumpResponse;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        dumpRequest = true;
        dumpResponse = true;
        log.info("dumpFilter init...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        long _start = System.currentTimeMillis();
        // DateTime _dt_start = DateTime.now();
        final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        // 过滤非业务请求，不打印info日志
        if (logFilter(httpRequest, servletResponse, filterChain)) {
            // 正常传递
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }


        BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpRequest);
        bufferedRequest.setCharacterEncoding("utf-8");
        // 打印请求日志
        info(bufferedRequest);

        // 请求体
        if (dumpRequest) log.info("Request body ->>>\n {} ", new String(bufferedRequest.getBuffer()));

        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final ByteArrayPrintWriter pw = new ByteArrayPrintWriter();
        HttpServletResponse wrappedResp = new HttpServletResponseWrapper(response) {
            public PrintWriter getWriter() {
                return pw.getWriter();
            }

            public ServletOutputStream getOutputStream() {
                return pw.getStream();
            }
        };

        // 正常传递
        filterChain.doFilter(bufferedRequest, wrappedResp);

        byte[] bytes = pw.toByteArray();
        response.getOutputStream().write(bytes);

        // 打印返回值
        if (dumpResponse) log.info("Response content:  ->>>\n{}", new String(bytes));

        log.info("本次请求耗时（毫秒）：{}", (System.currentTimeMillis() - _start));
    }

    @Override
    public void destroy() {
        log.info(getClass() + " destroy!");
    }

    /**
     * 过滤一些请求: 不获取RequestBody内容，日志级别降为debug
     *
     * @param httpRequest     .
     * @param servletResponse .
     * @param filterChain     .
     * @throws IOException      .
     * @throws ServletException .
     */
    private boolean logFilter(HttpServletRequest httpRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (httpRequest.getRequestURI().contains("/druid/")) {
            log.debug("druid 数据源监控，放行");
            debug(httpRequest);
//            filterChain.doFilter(httpRequest, servletResponse);
            return true;
        }

        // 健康，spring boot admin
        List<String> heaths = Lists.newArrayList(
                "/api/applications",
                "/health",
                "/info",
                "/heapdump",
                "/logfile",
                "/liquibase",
                "/flyway",
                "/metrics",
                "/env",
                "/refresh",
                "/jolokia/",
                "/trace");
        if (heaths.contains(httpRequest.getServletPath())) {
            log.debug("spring boot admin 健康监控相关请求，放行");
            debug(httpRequest);
//            filterChain.doFilter(httpRequest, servletResponse);
            return true;
        }

        String contentType = httpRequest.getContentType();
        if (contentType != null && !contentType.contains("application/json")) {
            log.debug("请求体内容不是JSON，过滤器将不读取，直接向下传递： {}", httpRequest.getContentType());
            return true;
        }

        // 静态资源过滤
        String staticResourceRegular = "^*[.jpg][.jsp][.html][.css][.js][.jpg][.jpeg][.png]*";
        Pattern pattern = Pattern.compile(staticResourceRegular);
        Matcher matcher = pattern.matcher(httpRequest.getServletPath());
        if (matcher.find()) {
            return true;
        }

        // 干脆把路径中含static/的请求全过滤
        if (httpRequest.getServletPath().contains("static/")) {
            return true;
        }

        return false;
    }


    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    private void debug(HttpServletRequest httpRequest) {
        String message =
                "\n-->                请求来源            : {} " +
                        "\n-->                请求地址            : {} " +
                        "\n-->                请求参数            : {} " +
                        "\n-->                httpMethod         : {} " +
                        "\n-->                JSessionId         : {} ";
        log.debug(message,
                getIp(httpRequest),
                httpRequest.getRequestURL(),
                httpRequest.getQueryString(),
                httpRequest.getMethod(),
                httpRequest.getSession().getId());
    }

    private void info(BufferedRequestWrapper bufferedRequest) {
        String message =
                "\n-->                请求来源            : {} " +
                        "\n-->                请求地址            : {} " +
                        "\n-->                请求参数            : {} " +
                        "\n-->                httpMethod          : {} " +
                        "\n-->                JSessionId          : {} ";
        log.info(message,
                getIp(bufferedRequest),
                bufferedRequest.getRequestURL(),
                bufferedRequest.getQueryString(),
                bufferedRequest.getMethod(),
                bufferedRequest.getSession().getId());
    }


}
