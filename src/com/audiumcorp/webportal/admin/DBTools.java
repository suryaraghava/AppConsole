package com.audiumcorp.webportal.admin;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

// Referenced classes of package com.audiumcorp.webportal.admin:
//            DesEncrypter, ApplicationVO, AudioVO

public final class DBTools
{

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/webportal";
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
    private static final String USER_ID = "user_id";
    private static final String APP_ID = "app_id";
    private static final String DESCRIPTION = "description";
    private static final String PATH = "path";
    private static final String AUDIO_TABLE = "audio";
    private static final String TBL_AUDIO = "AUDIO";

    private DBTools()
    {
    }

    protected static String encodeString(String stringToEncode)
    {
        String result1 = "";
        try
        {
            System.out.println((new StringBuilder("string to encrypt: ")).append(stringToEncode).toString());
            result1 = DesEncrypter.NAGASAKTI.encrypt(stringToEncode);
            System.out.println((new StringBuilder("encryt string in DBTools: ")).append(result1).toString());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return result1;
    }

    protected static String decodeString(String stringToDecode)
    {
        String result1 = "";
        try
        {
            result1 = DesEncrypter.NAGASAKTI.decrypt(stringToDecode);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return result1;
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

    protected static LinkedHashMap getAllApplications(String apin)
    {
        Connection conn;
        PreparedStatement statement;
        LinkedHashMap availableApps;
        conn = null;
        statement = null;
        availableApps = null;
        conn = getConnection();
        if(conn != null)
        {
            String sql = "";
            if(!apin.equalsIgnoreCase("0"))
            {
                sql = (new StringBuilder("SELECT app_name, app_description, name FROM applications, users WHERE applicatio" +
"ns.user_id=users.id AND users.pin= '"
)).append(apin).append("' ").append(" ORDER BY ").append("applications").append(".").append("id").toString();
            } else
            {
                sql = "SELECT app_name, app_description,'Administrator' FROM applications WHERE applica" +
"tions.user_id= 1 ORDER BY applications.id"
;
            }
            try
            {
                System.out.println((new StringBuilder("The Sql for getting applications")).append(sql).toString());
                statement = conn.prepareStatement(sql);
                ResultSet rs = statement.executeQuery();
                System.out.println("All Application query is executed");
                if(rs == null)
                {
                    System.out.println("Application resultset is null");
                }
                LinkedHashMap gapps = new LinkedHashMap();
                if(rs != null)
                {
                    for(; rs.next(); gapps.put(rs.getString(1), rs.getString(2))) { }
                }
                availableApps = gapps;
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
      
        cleanUp(conn, statement, null);
        return availableApps;
    }

    public static List getAllApplicationsByPin(String apin)
    {
        Connection conn;
        String sql;
        Statement stmt;
        ResultSet resultSet;
        List appList;
        conn = null;
        sql = "";
        stmt = null;
        resultSet = null;
        appList = new ArrayList();
        if(apin.equalsIgnoreCase("0"))
        {
            apin = "1";
        }
        sql = (new StringBuilder("SELECT applications.id AS id,app_name AS app_name, app_description AS app_descri" +
"ption, applications.user_id AS user_id FROM applications, users WHERE applicatio" +
"ns.user_id=users.id AND users.pin= '"
)).append(apin).append("' ").append(" ORDER BY ").append("applications").append(".").append("id").toString();
        System.out.println(sql);
        try
        {
            conn = getConnection();
            stmt = conn.createStatement();
            ApplicationVO applicationVO;
            for(resultSet = stmt.executeQuery(sql); resultSet.next(); appList.add(applicationVO))
            {
                applicationVO = new ApplicationVO();
                applicationVO.setId(resultSet.getInt("id"));
                applicationVO.setAppName(resultSet.getString("app_name"));
                applicationVO.setDescription(resultSet.getString("app_description"));
                applicationVO.setUserId(resultSet.getInt("user_id"));
            }

            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        cleanUp(conn, stmt, resultSet);
       
        return appList;
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
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(ClassNotFoundException cnfe)
        {
            cnfe.printStackTrace();
            return null;
        }
        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/webportal", "root", "spinsci");
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

    protected static List getAudiosOfApplication(String appId)
    {
        Connection conn;
        String sql;
        Statement stmt;
        ResultSet resultSet;
        List audioList;
        conn = null;
        sql = "";
        stmt = null;
        resultSet = null;
        audioList = new ArrayList();
        sql = (new StringBuilder("SELECT id,description,path,app_id FROM audio WHERE app_id=")).append(Integer.parseInt(appId)).toString();
        System.out.println(sql);
        try
        {
            conn = getConnection();
            stmt = conn.createStatement();
            AudioVO audioVO;
            for(resultSet = stmt.executeQuery(sql); resultSet.next(); audioList.add(audioVO))
            {
                audioVO = new AudioVO();
                audioVO.setId(resultSet.getInt("id"));
                audioVO.setDescription(resultSet.getString("description"));
                audioVO.setAppId(resultSet.getInt("app_id"));
                audioVO.setPath(resultSet.getString("path"));
            }

            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
      
        cleanUp(conn, stmt, resultSet);
        return audioList;
    }

    public static boolean removelogers(String filepath, String filename)
    {
        System.out.println((new StringBuilder(String.valueOf(filepath))).append("/").append(filename).toString());
        Document doc = null;
        SAXBuilder builder1 = new SAXBuilder();
        try
        {
            doc = builder1.build(new File((new StringBuilder(String.valueOf(filepath))).append("/").append(filename).toString()));
            Element root = doc.getRootElement();
            Element loggers = root.getChild("loggers");
            List logger_instance = loggers.getChildren();
            for(int i = 0; i < logger_instance.size(); i++)
            {
                Element logger_inst_ele = (Element)logger_instance.get(i);
                String name = logger_inst_ele.getAttributeValue("name");
                if(name.equals("CVPDatafeedLog"))
                {
                    logger_instance.remove(i);
                }
            }

            for(int k = 0; k < logger_instance.size(); k++)
            {
                Element logger_inst_ele = (Element)logger_instance.get(k);
                String name = logger_inst_ele.getAttributeValue("name");
                if(name.equals("CVPSNMPLog"))
                {
                    logger_instance.remove(k);
                }
            }

        }
        catch(JDOMException e)
        {
            e.printStackTrace();
            return false;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
        FileWriter fwrite = null;
        try
        {
            XMLOutputter outer = new XMLOutputter();
            fwrite = new FileWriter((new StringBuilder(String.valueOf(filepath))).append("/").append(filename).toString());
            outer.output(doc, fwrite);
            fwrite.close();
            fwrite = null;
        }
        catch(IOException e1)
        {
            if(fwrite != null)
            {
                try
                {
                    fwrite.close();
                    fwrite = null;
                }
                catch(Exception e11)
                {
                    e11.printStackTrace();
                }
            }
            e1.printStackTrace();
            return false;
        }
        return true;
    }

    protected static void deleteAudio(String audioId)
    {
        String sql;
        Connection connection;
        Statement statement;
        sql = (new StringBuilder("DELETE FROM audio WHERE id=")).append(Integer.parseInt(audioId)).toString();
        connection = getConnection();
        statement = null;
        try
        {
            statement = connection.createStatement();
            statement.execute(sql);
            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
     
        cleanUp(connection, statement, null);
    }

    protected static void insertAudioIntoDB(String destAppId, String path, String desc)
    {
        String sql;
        Connection connection;
        Statement statement;
        sql = (new StringBuilder("INSERT INTO TABLE audio(path,app_id,description) VALUES(")).append(path).append(",").append(destAppId).append(desc).append(")").toString();
        connection = getConnection();
        statement = null;
        try
        {
            statement = connection.createStatement();
            statement.execute(sql);
            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
     
        cleanUp(connection, statement, null);
    }

    protected static AudioVO getAudioOfById(String audioId)
    {
        String sql;
        Connection connection;
        Statement statement;
        AudioVO audioVO;
        sql = (new StringBuilder("SELECT id , path , description , app_id FROM audio WHERE id = ")).append(audioId).toString();
        connection = getConnection();
        statement = null;
        ResultSet resultSet = null;
        audioVO = new AudioVO();
        try
        {
            statement = connection.createStatement();
             resultSet = statement.executeQuery(sql);
            if(resultSet.next())
            {
                audioVO.setId(resultSet.getInt("id"));
                audioVO.setDescription(resultSet.getString("description"));
                audioVO.setPath(resultSet.getString("path"));
                audioVO.setAppId(resultSet.getInt("app_id"));
            }
            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
     
        cleanUp(connection, statement, null);
        return audioVO;
    }

    protected static void moveAudio(String sourceAppId, String destAppId, String audioId)
    {
        String sql;
        Connection connection;
        Statement statement;
        sql = (new StringBuilder("UPDATE audio SET app_id = ")).append(destAppId).append(" WHERE ").append("id").append(" = ").append(destAppId).toString();
        connection = getConnection();
        statement = null;
        ResultSet resultSet = null;
        try
        {
            statement = connection.createStatement();
            statement.execute(sql);
            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
      
        cleanUp(connection, statement, null);
    }

    private static AudioVO getAudioByPath(String path)
    {
        String sql;
        Connection connection;
        Statement statement;
        AudioVO audioVO;
        sql = (new StringBuilder("SELECT id , path , description , app_id FROM audio WHERE path = '")).append(path).append("'").toString();
        connection = getConnection();
        statement = null;
        ResultSet resultSet = null;
        audioVO = new AudioVO();
        try
        {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if(resultSet.next())
            {
                audioVO.setId(resultSet.getInt("id"));
                audioVO.setDescription(resultSet.getString("description"));
                audioVO.setPath(resultSet.getString("path"));
                audioVO.setAppId(resultSet.getInt("app_id"));
            }
           
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
      
        cleanUp(connection, statement, null);
        return audioVO;
    }

    protected static boolean saveAudio(AudioVO audioVO)
        throws SQLException
    {
        Exception exception;
        if(getAudioByPath(audioVO.getPath()).getAppId() == audioVO.getAppId())
        {
            return false;
        }
        String sql = (new StringBuilder("INSERT INTO AUDIO(app_id, description, path) VALUES (")).append(audioVO.getAppId()).append(", ").append("\"").append(audioVO.getDescription()).append("\", ").append("\"").append(audioVO.getPath()).append("\")").toString();
        Connection connection = getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            statement = connection.createStatement();
            statement.execute(sql);
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally
        {
            cleanUp(connection, statement, null);
        }
        cleanUp(connection, statement, null);
        return true;
      
    }

    public static void main(String args[])
        throws Exception
    {
        DBTools fr = new DBTools();
        AudioVO audioVO = new AudioVO();
        audioVO.setAppId(234);
        audioVO.setPath(" c:&#92;audio&#92;5120&#92;234&#92;5120ulaw1.wav");
        saveAudio(audioVO);
    }
}
