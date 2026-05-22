package com.trivia.api.filter;

import javax.ejb.Schedule;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


// Limits the request rate per IP in a given time frame.
@WebFilter(filterName = "RateLimiterFilter", urlPatterns = {"/*"}, initParams = {@WebInitParam(name = "mood", value = "awake")})
public class RateLimiterFilter implements Filter {
    private static final Long limitRate = 1_000L;
    private static final String errorMessage = "Status error code 429: Too Many Requests. Try again later";
    private ConcurrentHashMap<String, Long> visitorsRequests;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String userName = request.getRemoteAddr();

        if (visitorsRequests.containsKey(userName)) {
            visitorsRequests.put(userName, visitorsRequests.get(userName) + 1);
        }
        else {
            visitorsRequests.put(userName, 1L);
        }

        if (visitorsRequests.get(userName) > limitRate) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendError(Integer.parseInt(Response.Status.TOO_MANY_REQUESTS.toString()), errorMessage);
        }
        else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        visitorsRequests = new ConcurrentHashMap<>();
    }

    @Override
    public void destroy() {

    }

    @Schedule(hour = "*")
    public void clearFilter() {
        visitorsRequests.clear();
    }
}