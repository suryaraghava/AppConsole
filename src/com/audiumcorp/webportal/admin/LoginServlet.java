package com.audiumcorp.webportal.admin;

import java.io.IOException;
import java.util.LinkedHashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

// Referenced classes of package com.audiumcorp.webportal.admin:
//            DBTools

public class LoginServlet extends HttpServlet
{

    private static final long serialVersionUID = 0x24d87e9c1e2343a2L;

    public LoginServlet()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String pin = DBTools.getUserPin(email);
        HttpSession session = request.getSession(true);
        String TempmaxApps1 = DBTools.loadSetting("Max allowed applications");
        session.setAttribute("maxApps", TempmaxApps1);
        String ap = (String)session.getAttribute("maxApps");
        String redirectPage = "";
        if(validUser(pin, password))
        {
            session.setAttribute("pin", pin);
            LinkedHashMap myapps = loadApplications(pin);
            session.setAttribute("myApps", myapps);
            if(DBTools.isAdmin(pin))
            {
                java.util.HashMap configOptions = DBTools.loadAllSettings();
                session.setAttribute("configOptions", configOptions);
                session.setAttribute("admin", "true");
            } else
            {
                session.setAttribute("admin", "false");
                LinkedHashMap globalApps = loadApplications("0");
                session.setAttribute("globalApps", globalApps);
            }
            redirectPage = "myapps.jsp";
        } else
        {
            session.setAttribute("failedLoginAttempt", "true");
            redirectPage = "index.jsp";
        }
        request.getRequestDispatcher((new StringBuilder("/")).append(redirectPage).toString()).forward(request, response);
    }

    private LinkedHashMap loadApplications(String pin)
    {
        return DBTools.getAllApplications(pin);
    }

    private boolean validUser(String pin, String password)
    {
        return DBTools.isUserActive(pin, password);
    }
}
