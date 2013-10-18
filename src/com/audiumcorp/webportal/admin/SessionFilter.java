package com.audiumcorp.webportal.admin;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

public final class SessionFilter
    implements Filter
{

    public SessionFilter()
    {
    }

    public void destroy()
    {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        javax.servlet.http.HttpSession session = ((HttpServletRequest)request).getSession(false);
        if(session == null)
        {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else
        {
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig filterconfig)
        throws ServletException
    {
    }
}
