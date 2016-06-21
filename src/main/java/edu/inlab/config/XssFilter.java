package edu.inlab.config;

import org.owasp.esapi.ESAPI;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Reference: https://defensivecode.wordpress.com/2013/09/03/spring-security-xss/
 */

public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest)servletRequest), servletResponse);
    }

    @Override
    public void destroy() {

    }

}
