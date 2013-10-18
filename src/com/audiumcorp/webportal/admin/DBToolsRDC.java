package com.audiumcorp.webportal.admin;

import java.security.MessageDigest;
import java.sql.*;
import java.util.*;

// Referenced classes of package com.audiumcorp.webportal.admin:
//            RDCConfig

public final class DBToolsRDC
{

    private static final String DB_DRIVER = "org.gjt.mm.mysql.Driver";
    private static final String DB_URL = "jdbc:mysql://192.168.50.10:3306/webportal";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "spinsci";
    private static final String COL_ID = "id";
    private static final String TBL_USERS = "users";
    private static final String COL_PIN = "pin";
    private static final String COL_PASSWORD = "password";
    private static final String COL_NAME = "name";
    private static final String COL_ACTIVE = "active";
    private static final String COL_EMAIL = "email";
    private static final String COL_ACTIVATION_CODE = "act_code";
    private static final String TBL_APPLICATIONS = "applications";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_APP_NAME = "app_name";
    private static final String COL_APP_DESCRIPTION = "app_description";
    private static final String TBL_CONFIGURATION = "configuration";
    private static final String COL_CONF_NAME = "config_name";
    private static final String COL_CONF_VALUE = "config_value";
    private static final String COL_CONF_CHANGEABLE = "config_changeable";
    protected static final String DB_ACTION_DEPLOY = "deploy";
    protected static final String DB_ACTION_UPDATE = "update";

    private DBToolsRDC()
    {
    }

    protected static String encodeString(String stringToEncode)
    {
        StringBuffer result = new StringBuffer();
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte bpwd[] = stringToEncode.getBytes("UTF-8");
            md.update(bpwd);
            byte encoded[] = md.digest();
            for(int i = 0; i < encoded.length; i++)
            {
                result.append(Integer.toHexString(encoded[i] & 0xff));
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return result.toString();
    }

    public static RDCConfig getAvailableRDCConfig()
    {
        RDCConfig rdcConfig;
        Connection conn;
        PreparedStatement s;
        rdcConfig = new RDCConfig();
        conn = null;
        s = null;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = "SELECT * FROM rdcconfig r where r.available=0 ";
                s = conn.prepareStatement(sql);
                ResultSet rs = s.executeQuery();
                if(rs.next())
                {
                    rdcConfig.setId(rs.getInt(1));
                    rdcConfig.setName(rs.getString(2));
                    rdcConfig.setIpAddress(rs.getString(3));
                    rdcConfig.setInUse(false);
                }
            }
            
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
        }
        cleanUp(conn, s, null);
       
        return rdcConfig;
    }

    public static boolean updateRDPConfig(int value, String ipAddress)
    {
        Connection conn;
        PreparedStatement s;
        RDCConfig rdcConfig = new RDCConfig();
        conn = null;
        s = null;
        int n;
        conn = getConnection();
        try{
        if(conn != null)
        {
        	 String sql = "update webportal.rdcconfig set available=? where ipaddress=?";
             s = conn.prepareStatement(sql);
             s.setInt(1, value);
             s.setString(2, ipAddress);
             n = s.executeUpdate();
             if(n == 0)
             {
                 cleanUp(conn, s, null);
                 return false;
             }
            
        }
       
       
        cleanUp(conn, s, null);
        }
        catch(Exception e)
        {}
        return true;
    }

    protected static boolean isUserActive(String pin, String password)
    {
        boolean isUserActive;
        Connection conn;
        PreparedStatement s;
        isUserActive = false;
        conn = null;
        s = null;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = (new StringBuilder("SELECT active FROM users WHERE pin=\"")).append(pin).append("\"").append(" AND ").append("password").append("=\"").append(encodeString(password)).append("\"").toString();
                s = conn.prepareStatement(sql);
                ResultSet rs = s.executeQuery();
                if(rs.next())
                {
                    isUserActive = rs.getBoolean(1);
                }
            }
            
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
        }
      
        cleanUp(conn, s, null);
        return isUserActive;
    }

    protected static String getAdminPin()
    {
        String userPin;
        Connection conn;
        PreparedStatement s;
        userPin = "";
        conn = null;
        s = null;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = "SELECT pin FROM users, configuration WHERE users.email=configuration.config_valu" +
"e AND configuration.config_name='Administrator email'"
;
                s = conn.prepareStatement(sql);
                ResultSet rs = s.executeQuery();
                if(rs.next())
                {
                    userPin = rs.getString(1);
                }
            }
            
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
        }
      
        cleanUp(conn, s, null);
        return userPin;
    }

    protected static String getUserPin(String email)
    {
        String userPin;
        Connection conn;
        PreparedStatement s;
        userPin = "";
        conn = null;
        s = null;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = (new StringBuilder("SELECT pin FROM users WHERE email=\"")).append(encodeString(email)).append("\"").toString();
                s = conn.prepareStatement(sql);
                ResultSet rs = s.executeQuery();
                if(rs.next())
                {
                    userPin = rs.getString(1);
                }
            }
            
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
        }
      
        cleanUp(conn, s, null);
        return userPin;
    }

    protected static String getUserName(String email)
    {
        String userName;
        Connection conn;
        PreparedStatement s;
        userName = "";
        conn = null;
        s = null;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = (new StringBuilder("SELECT name FROM users WHERE email=\"")).append(encodeString(email)).append("\"").toString();
                s = conn.prepareStatement(sql);
                ResultSet rs = s.executeQuery();
                if(rs.next())
                {
                    userName = rs.getString(1);
                }
            }
            
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
        }
      
        cleanUp(conn, s, null);
        return userName;
    }

    protected static boolean pinExists(long pinToCheck)
    {
        boolean exists;
        Connection conn;
        PreparedStatement s;
        exists = false;
        conn = null;
        s = null;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = (new StringBuilder("SELECT id FROM users WHERE pin=\"")).append(pinToCheck).append("\"").toString();
                s = conn.prepareStatement(sql);
                ResultSet rs = s.executeQuery();
                exists = rs.next();
            }
            
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
        }
      
        cleanUp(conn, s, null);
        return exists;
    }

    protected static String getEmail(String pin)
    {
        String email;
        Connection conn;
        PreparedStatement s;
        email = "";
        conn = null;
        s = null;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = (new StringBuilder("SELECT email FROM users WHERE pin=\"")).append(pin).append("\"").toString();
                s = conn.prepareStatement(sql);
                ResultSet rs = s.executeQuery();
                if(rs.next())
                {
                    email = rs.getString(1);
                }
            }
            
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
        }
      
        cleanUp(conn, s, null);
        return email;
    }

    protected static boolean accountExists(String emailToCheck)
    {
        boolean exists;
        Connection conn;
        PreparedStatement s;
        exists = false;
        conn = null;
        s = null;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = (new StringBuilder("SELECT id FROM users WHERE email=\"")).append(encodeString(emailToCheck)).append("\"").toString();
                s = conn.prepareStatement(sql);
                ResultSet rs = s.executeQuery();
                exists = rs.next();
            }
            
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
        }
       
        cleanUp(conn, s, null);
        return exists;
    }

    protected static boolean resetPassword(String pin, String password)
    {
        boolean result;
        Connection conn;
        PreparedStatement statement;
        result = false;
        conn = null;
        statement = null;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = (new StringBuilder("UPDATE users SET password=\"")).append(encodeString(password)).append("\"").append(" WHERE ").append("pin").append("=\"").append(pin).append("\"").toString();
                statement = conn.prepareStatement(sql);
                result = statement.executeUpdate() > 0;
            }
            
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
        }
     
        cleanUp(conn, statement, null);
        return result;
    }

    protected static void updateSettings(Map newSettings)
        throws SQLException
    {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        String sql = "";
        if(conn != null)
        {
            for(Iterator it = newSettings.keySet().iterator(); it.hasNext();)
            {
                String name = (String)it.next();
                String value[] = (String[])newSettings.get(name);
                if(name.equals("Administrator email"))
                {
                    if(!value[0].equals(""))
                    {
                        String adminEmail = encodeString(value[0]);
                        String adminPin = getAdminPin();
                        String adminChangeUserTable = (new StringBuilder("UPDATE users SET email=\"")).append(adminEmail).append("\"").append(" WHERE ").append("pin").append("=").append(adminPin).toString();
                        String adminChangeConfigTable = (new StringBuilder("UPDATE configuration SET config_value=\"")).append(adminEmail).append("\"").append(" WHERE ").append("config_name").append("=\"").append(name).append("\"").toString();
                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate(adminChangeUserTable);
                        stmt.executeUpdate(adminChangeConfigTable);
                    }
                } else
                {
                    String valueToInsert = value[0].replace('"', '\'');
                    sql = (new StringBuilder("UPDATE configuration SET config_value=\"")).append(valueToInsert).append("\"").append(" WHERE ").append("config_name").append("=\"").append(name).append("\"").toString();
                    statement = conn.createStatement();
                    statement.executeUpdate(sql);
                }
            }

        }
        cleanUp(conn, statement, null);
    }

    protected static String loadSetting(String settingName)
    {
        String maxApps;
        Connection conn;
        PreparedStatement s;
        maxApps = "";
        conn = null;
        s = null;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = (new StringBuilder("SELECT config_value FROM configuration WHERE config_name=\"")).append(settingName).append("\"").toString();
                s = conn.prepareStatement(sql);
                ResultSet rs = s.executeQuery();
                if(rs.next())
                {
                    maxApps = rs.getString("config_value");
                }
            }
            
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
        }
   
        cleanUp(conn, s, null);
        return maxApps;
    }

    public static HashMap loadAllSettings()
    {
        HashMap configOptions;
        Connection conn;
        PreparedStatement s;
        configOptions = new HashMap();
        conn = null;
        s = null;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = "SELECT config_name, config_value FROM configuration WHERE config_changeable= 1";
                s = conn.prepareStatement(sql);
                String name;
                String value;
                for(ResultSet rs = s.executeQuery(); rs.next(); configOptions.put(name, value))
                {
                    name = rs.getString("config_name");
                    value = rs.getString("config_value");
                    if(name.equalsIgnoreCase("Administrator email"))
                    {
                        value = "";
                    }
                }

            }
            
        }
        catch(Exception sqlEx)
        {
            sqlEx.printStackTrace();
        }
    
        cleanUp(conn, s, null);
        return configOptions;
    }

    protected static ResultSet getAllApplications()
    {
        Connection conn;
        PreparedStatement statement;
        ResultSet availableApps;
        conn = null;
        statement = null;
        availableApps = null;
        conn = getConnection();
        if(conn != null)
        {
            String sql = "SELECT app_name, app_description, name FROM applications, users WHERE applicatio" +
"ns.user_id=users.id ORDER BY applications.id"
;
            try
            {
                statement = conn.prepareStatement(sql);
                ResultSet rs = statement.executeQuery();
                availableApps = rs;
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
    
        cleanUp(conn, statement, null);
        return availableApps;
    }

    protected static HashMap loadEmailSettings()
    {
        Connection conn;
        PreparedStatement statement;
        HashMap settings;
        conn = null;
        statement = null;
        settings = new HashMap();
        conn = getConnection();
        if(conn != null)
        {
            String sql = "SELECT config_name, config_value FROM configuration WHERE config_name LIKE '%ema" +
"il%'"
;
            try
            {
                statement = conn.prepareStatement(sql);
                for(ResultSet rs = statement.executeQuery(); rs.next(); settings.put(rs.getString("config_name"), rs.getString("config_value"))) { }
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
    
        cleanUp(conn, statement, null);
        return settings;
    }

    protected static void insertAppIntoDB(String pin, String appName, String appDesc, boolean update)
    {
        Connection conn;
        PreparedStatement statement;
        String selectUserIDQuery;
        conn = null;
        statement = null;
        selectUserIDQuery = (new StringBuilder("(SELECT id FROM users WHERE pin=")).append(pin).append(")").toString();
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                if(appName.indexOf('"') > -1)
                {
                    appName = appName.replace('"', '\'');
                }
                if(appDesc.indexOf('"') > -1)
                {
                    appDesc = appDesc.replace('"', '\'');
                }
                String sql = "";
                if(!update)
                {
                    sql = (new StringBuilder("INSERT INTO applications(user_id, app_name, app_description) VALUES (")).append(selectUserIDQuery).append(", ").append("\"").append(appName).append("\", ").append("\"").append(appDesc).append("\")").toString();
                } else
                {
                    sql = (new StringBuilder("UPDATE applications SET app_description=\"")).append(appDesc).append("\"").append(" WHERE ").append("user_id").append("=").append(selectUserIDQuery).append(" AND ").append("app_name").append("=\"").append(appName).append("\"").toString();
                }
                statement = conn.prepareStatement(sql);
                statement.execute();
            }
            
        }
        catch(SQLException sql)
        {
            sql.printStackTrace();
        }
   
        cleanUp(conn, statement, null);
    }

    protected static boolean activateUser(String pin, String activationCode)
    {
        Connection conn;
        PreparedStatement statement;
        boolean result;
        conn = null;
        statement = null;
        result = false;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = (new StringBuilder("UPDATE users SET active=1 WHERE pin=")).append(pin).append(" AND ").append("act_code").append("=").append(activationCode).toString();
                statement = conn.prepareStatement(sql);
                result = statement.executeUpdate() > 0;
            }
            
        }
        catch(SQLException sql)
        {
            sql.printStackTrace();
        }
      
        cleanUp(conn, statement, null);
        return result;
    }

    protected static void removeAppFromDB(String appName)
    {
        Connection conn;
        PreparedStatement statement;
        conn = null;
        statement = null;
        try
        {
            conn = getConnection();
            if(conn != null)
            {
                String sql = (new StringBuilder("DELETE FROM applications WHERE app_name=\"")).append(appName).append("\"").toString();
                statement = conn.prepareStatement(sql);
                statement.execute();
            }
            
        }
        catch(SQLException sql)
        {
            sql.printStackTrace();
        }
      
        cleanUp(conn, statement, null);
    }

    protected static void createUser(String pin, String pwd, String name, String email, String activationCode)
    {
        Connection conn;
        PreparedStatement statement;
        conn = null;
        statement = null;
        try
        {
            conn = getConnection();
            if(name.indexOf('"') > -1)
            {
                name = name.replace('"', '\'');
            }
            if(conn != null)
            {
                String sql = (new StringBuilder("INSERT INTO users(pin, password, name, email, act_code) VALUES (\"")).append(pin).append("\", ").append("\"").append(encodeString(pwd)).append("\", ").append("\"").append(name).append("\", ").append("\"").append(encodeString(email)).append("\", ").append("\"").append(activationCode).append("\")").toString();
                statement = conn.prepareStatement(sql);
                statement.execute();
            }
            
        }
        catch(SQLException sql)
        {
            sql.printStackTrace();
        }
     
        cleanUp(conn, statement, null);
    }

    private static Connection getConnection()
    {
        Connection conn = null;
        try
        {
            Class.forName("org.gjt.mm.mysql.Driver");
        }
        catch(ClassNotFoundException cnfe)
        {
            cnfe.printStackTrace();
            return null;
        }
        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://192.168.50.10:3306/webportal", "root", "spinsci");
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        return conn;
    }

    private static void cleanUp(Connection c, Statement s, ResultSet r)
    {
        try
        {
            if(r != null)
            {
                r.close();
            }
            if(s != null)
            {
                s.close();
            }
            if(c != null)
            {
                c.close();
            }
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
    }

    public static boolean isAdmin(String pin)
    {
        String currentEmail = getEmail(pin);
        String adminEmail = loadSetting("Administrator email");
        return currentEmail.equalsIgnoreCase(adminEmail);
    }
}
