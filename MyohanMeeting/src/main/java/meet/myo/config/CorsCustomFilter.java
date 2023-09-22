package meet.myo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CorsCustomFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
//
//@WebFilter("/filter-response-header/*")
//public class AddResponseHeaderFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response,
//                         FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setHeader(
//                "Baeldung-Example-Filter-Header", "Value-Filter");
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // ...
//    }
//
//    @Override
//    public void destroy() {
//        // ...
//    }
//}
//
