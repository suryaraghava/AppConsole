package com.audiumcorp.webportal.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

// Referenced classes of package com.audiumcorp.webportal.admin:
//            DBTools

public class UserServlet extends HttpServlet
{

    private static final long serialVersionUID = 0xe9a596e43a2a8d49L;

    public UserServlet()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        HttpSession session = request.getSession(true);
        String redirectPage = "index.jsp";
        String mode = request.getParameter("mode");
        if(mode == null)
        {
            redirectPage = "index.jsp";
        } else
        if(mode.equals("activate"))
        {
            String pin = request.getParameter("pin");
            String actCode = request.getParameter("actCode");
            if(DBTools.activateUser(pin, actCode))
            {
                session.setAttribute("status", "activationDone");
            } else
            {
                session.setAttribute("status", "activationFailed");
            }
            redirectPage = "status.jsp";
        } else
        if(mode.equals("register"))
        {
            boolean errorInForm = false;
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String password2 = request.getParameter("password2");
            if(password.equals("") || !password.equals(password2))
            {
                errorInForm = true;
                session.setAttribute("WRONG_PASSWORD", "true");
            }
            if(!validEmail(email.toLowerCase()))
            {
                errorInForm = true;
                session.setAttribute("WRONG_EMAIL", "true");
            } else
            {
                session.setAttribute("EMAIL", email);
            }
            if(DBTools.accountExists(email))
            {
                errorInForm = true;
                session.setAttribute("ACCOUNT_EXISTS", "true");
            }
            if(errorInForm)
            {
                session.setAttribute("NAME", name);
                redirectPage = "register.jsp";
            } else
            {
                String websiteURL = "";
                String port = "";
                if(request.getServerPort() != 80)
                {
                    port = (new StringBuilder(":")).append(request.getServerPort()).toString();
                }
                if(request.isSecure())
                {
                    websiteURL = (new StringBuilder("https://")).append(request.getServerName()).append(port).toString();
                } else
                {
                    websiteURL = (new StringBuilder("http://")).append(request.getServerName()).append(port).toString();
                }
                createUser(name, password, email, websiteURL);
                session.setAttribute("status", "userCreated");
                redirectPage = "status.jsp";
            }
        } else
        if(mode.equals("passwordReminder"))
        {
            String email = request.getParameter("email");
            String pin = DBTools.getUserPin(email);
            String password = createTempPassword();
            if(DBTools.resetPassword(pin, password))
            {
                sendPasswordReminder(email, pin, password);
                session.setAttribute("status", "passwordReminderSent");
            } else
            {
                session.setAttribute("status", "passwordReminderError");
            }
            redirectPage = "status.jsp";
        } else
        if(mode.equals("passwordChange"))
        {
            String pin = (String)session.getAttribute("pin");
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            String newPassword2 = request.getParameter("newPassword2");
            if(newPassword.equals("") || !newPassword.equals(newPassword2))
            {
                session.setAttribute("PASSWORD_DOESNT_MATCH", "true");
            } else
            if(DBTools.isUserActive(pin, oldPassword))
            {
                DBTools.resetPassword(pin, newPassword);
                session.setAttribute("PASSWORD_CHANGED", "true");
            } else
            {
                session.setAttribute("WRONG_OLD_PASSWORD", "true");
            }
            redirectPage = "config.jsp";
        }
        request.getRequestDispatcher((new StringBuilder("/")).append(redirectPage).toString()).forward(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doPost(request, response);
    }

    private String createTempPassword()
    {
        StringBuffer pwd = new StringBuffer();
        for(int i = 0; i < 9; i++)
        {
            long candidate = 0L;
            do
            {
                double randNumber = Math.random() * 100D;
                candidate = Math.round(randNumber);
            } while(candidate < 65L || candidate > 90L);
            pwd.append((char)(int)candidate);
        }

        return pwd.toString();
    }

    private void createUser(String name, String password, String email, String websiteURL)
    {
        long candidate;
        do
        {
            double randNumber = Math.random() * 10000D;
            candidate = Math.round(randNumber);
        } while(candidate < 1000L || candidate >= 10000L || DBTools.pinExists(candidate));
        String pin = Long.toString(candidate);
        do
        {
            double randNumber = Math.random() * 10000000D;
            candidate = Math.round(randNumber);
        } while(candidate < 0xf4240L || candidate >= 0x5f5e100L);
        String activationCode = Long.toString(candidate);
        DBTools.createUser(pin, password, name, email, activationCode);
        sendActivationEmail(email, name, pin, activationCode, websiteURL);
    }

    private boolean validEmail(String email)
    {
        boolean result = false;
        String validDomains1 = DBTools.loadSetting("Not Allowed domains").toLowerCase();
        String validDomains = DBTools.loadSetting("Allowed domains").toLowerCase();
        int atLocation = email.indexOf("@");
        if(atLocation > -1 && email.lastIndexOf(".") > atLocation)
        {
            String domains[] = validDomains.split(",");
            for(int x = 0; x < domains.length; x++)
            {
                if(email.indexOf(domains[x].trim()) > -1)
                {
                    result = true;
                }
            }

            String domains1[] = validDomains1.split(",");
            for(int xx = 0; xx < domains1.length; xx++)
            {
                if(email.indexOf(domains1[xx].trim()) > -1)
                {
                    result = false;
                }
            }

        }
        return result;
    }

    private void sendActivationEmail(String email, String name, String pin, String activationCode, String websiteURL)
    {
        try
        {
            HashMap emailSettings = DBTools.loadEmailSettings();
            String fromEmail = (String)emailSettings.get("email_from_address");
            String subject = (String)emailSettings.get("Activation email subject");
            StringBuffer body = new StringBuffer((String)emailSettings.get("Activation email body"));
            Properties properties = new Properties();
            properties.put("mail.transport.protocol", emailSettings.get("email_protocol"));
            properties.put("mail.smtp.host", emailSettings.get("email_host"));
            Session session = Session.getInstance(properties, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject);
            String activationURL = (new StringBuilder(String.valueOf(websiteURL))).append("/DevPortal/User?mode=activate&pin=").append(pin).append("&actCode=").append(activationCode).toString();
            try
            {
                body.replace(body.indexOf("{NAME}"), body.indexOf("{NAME}") + 6, name);
            }
            catch(Exception exception) { }
            try
            {
                body.replace(body.indexOf("{PIN}"), body.indexOf("{PIN}") + 5, pin);
            }
            catch(Exception ex)
            {
                body.append((new StringBuilder("\nPIN: ")).append(pin).toString());
            }
            try
            {
                body.replace(body.indexOf("{ACTIVATION_URL}"), body.indexOf("{ACTIVATION_URL}") + 16, activationURL);
            }
            catch(Exception ex)
            {
                body.append((new StringBuilder("\nActivation URL: ")).append(activationURL).toString());
            }
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(body.toString());
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            message.setContent(mp);
            message.saveChanges();
            Transport.send(message);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sendPasswordReminder(String email, String pin, String password)
    {
        try
        {
            HashMap emailSettings = DBTools.loadEmailSettings();
            String fromEmail = (String)emailSettings.get("email_from_address");
            String subject = (String)emailSettings.get("Password reminder email subject");
            StringBuffer body = new StringBuffer((String)emailSettings.get("Password reminder email body"));
            Properties properties = new Properties();
            properties.put("mail.transport.protocol", emailSettings.get("email_protocol"));
            properties.put("mail.smtp.host", emailSettings.get("email_host"));
            Session session = Session.getInstance(properties, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject);
            try
            {
                body.replace(body.indexOf("{NAME}"), body.indexOf("{NAME}") + 6, DBTools.getUserName(email));
            }
            catch(Exception exception) { }
            try
            {
                body.replace(body.indexOf("{PIN}"), body.indexOf("{PIN}") + 5, pin);
            }
            catch(Exception ex)
            {
                body.append((new StringBuilder("\nYour new password: ")).append(password).toString());
            }
            try
            {
                body.replace(body.indexOf("{PASSWORD}"), body.indexOf("{PASSWORD}") + 10, password);
            }
            catch(Exception ex)
            {
                body.append((new StringBuilder("\nYour new password: ")).append(password).toString());
            }
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(body.toString());
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            message.setContent(mp);
            message.saveChanges();
            Transport.send(message);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
