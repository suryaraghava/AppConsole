package com.audiumcorp.webportal.admin;

import com.audium.client.admin.AppDelete;
import java.io.*;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

// Referenced classes of package com.audiumcorp.webportal.admin:
//            DBTools

public class RemoveServlet extends HttpServlet
{

    private static final long serialVersionUID = 0x2f8b270aa931d5dfL;
    private static final String APPS_FOLDER = "applications";
    private static final String ADMIN_FOLDER = "admin";
    private static final String APPLICATION_REMOVED = "has been fully released from memory";
    private static final String APPLICATION_SUSPENDED = "has been suspended";
    private static final String STATUS_OK = "OK";
    private static final String ACTIVE_CALLERS = "active callers";

    public RemoveServlet()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        Enumeration paramNames = request.getParameterNames();
        HttpSession session = request.getSession(false);
        while(paramNames.hasMoreElements()) 
        {
            String name = (String)paramNames.nextElement();
            if(request.getParameter(name).equalsIgnoreCase("checkbox"))
            {
                removeApp(session, name);
            }
        }
        request.getRequestDispatcher("/myapps.jsp").forward(request, response);
    }

    private void removeApp(HttpSession curSession, String appName)
    {
        LinkedHashMap apps = (LinkedHashMap)curSession.getAttribute("myApps");
        String status = undeployApp(appName);
        if(status.equals("OK"))
        {
            DBTools.removeAppFromDB(appName);
            apps.remove(appName);
            String appName1;
            for(Iterator it = apps.keySet().iterator(); it.hasNext(); System.out.println((new StringBuilder("app name is ")).append(appName1).toString()))
            {
                appName1 = (String)it.next();
            }

        } else
        if(status.equals("active callers"))
        {
            curSession.setAttribute("activeCalls", appName);
        }
        curSession.setAttribute("myApps", apps);
    }

    private String undeployApp(String appName)
    {
        String audiumHome = DBTools.loadSetting("audium_home");
        String removePath = (new StringBuilder(String.valueOf(addPathSeparator(audiumHome)))).append(addPathSeparator("applications")).append(addPathSeparator(appName)).append(addPathSeparator("admin")).toString();
        String params[] = {
            removePath, "noconfirm", "nocountdown"
        };
        PrintStream currentPS = System.out;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
        AppDelete.main(params);
        System.setOut(currentPS);
        String result = bos.toString();
        System.out.println((new StringBuilder("result from app remove")).append(result).toString());
        System.out.println("----------------------");
        if(result.indexOf("has been fully released from memory") > -1)
        {
            File appFolder = new File((new StringBuilder(String.valueOf(addPathSeparator(audiumHome)))).append(addPathSeparator("applications")).append(appName).toString());
            deleteDir(appFolder);
            return "OK";
        }
        if(result.indexOf("has been suspended") > -1)
        {
            return "active callers";
        } else
        {
            return "failed";
        }
    }

    private boolean deleteDir(File file)
    {
        if(file.isDirectory())
        {
            String fileList[] = file.list();
            for(int i = 0; i < fileList.length; i++)
            {
                boolean success = deleteDir(new File(file, fileList[i]));
                if(!success)
                {
                    return false;
                }
            }

        }
        return file.delete();
    }

    private String addPathSeparator(String path)
    {
        if(!path.endsWith(File.separator))
        {
            path = (new StringBuilder(String.valueOf(path))).append(File.separator).toString();
        }
        return path;
    }
}
