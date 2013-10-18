package com.audiumcorp.webportal.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

// Referenced classes of package com.audiumcorp.webportal.admin:
//            DBTools

public class ConfigServlet extends HttpServlet
{

    private static final long serialVersionUID = 0x6495197c6a411bccL;

    public ConfigServlet()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        HttpSession session = request.getSession(true);
        Map configSettingsMap = request.getParameterMap();
        String resultPage = "";
        try
        {
            updateConfig(configSettingsMap);
            java.util.HashMap configOptions = DBTools.loadAllSettings();
            session.setAttribute("configOptions", configOptions);
            resultPage = "configUpdated.jsp";
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            resultPage = "configFailed.jsp";
        }
        request.getRequestDispatcher((new StringBuilder("/")).append(resultPage).toString()).forward(request, response);
    }

    private void updateConfig(Map configSettings)
        throws SQLException
    {
        DBTools.updateSettings(configSettings);
    }
}
