package com.audiumcorp.webportal.admin;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.sound.sampled.*;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

// Referenced classes of package com.audiumcorp.webportal.admin:
//            PropertyFileParser, DBTools, AudioVO

public class AudioServlet extends HttpServlet
{

    private static final String DESCRITPION_FIELD = "description";
    private static final String ACTION_FIELD = "appAction";
    private static final String PATH = "AUDIO_PATH";
    private static final String TEMP_PATH = "TEMP_PATH";
    private static final String APPS_FOLDER = "applications";
    private static final int MONO = 1;
    private static final String VALID_FORMATS = PropertyFileParser.getProperty("VALID_AUDIO_FORMATS");
    private static final String SEPERATOR = ",";
    private static final String ZIP = "zip";
    private static Logger logger = Logger.getLogger(com.audiumcorp.webportal.admin.AudioServlet.class);

    public AudioServlet()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        String appName;
        FileItem item;
        String pin;
        String appId;
        DiskFileUpload upload;
        String audioFolder;
        boolean isAudio;
        StringBuffer messageBuffer;
        String prevrequest;
        FileOutputStream out;
        File fileToSave;
        boolean updateApp = false;
        appName = "";
        item = null;
        HttpSession session = request.getSession(false);
        pin = (String)session.getAttribute("pin");
        appId = "";
        upload = new DiskFileUpload();
        audioFolder = PropertyFileParser.getProperty("AUDIO_PATH");
        isAudio = false;
        messageBuffer = new StringBuffer();
        prevrequest = "";
        out = null;
        fileToSave = null;
        try
        {
            List items = upload.parseRequest(request);
            for(Iterator iter = items.iterator(); iter.hasNext();)
            {
                FileItem tempItem = (FileItem)iter.next();
                if(tempItem.isFormField())
                {
                    if(tempItem.getFieldName().equalsIgnoreCase("appAction"))
                    {
                        if(tempItem.getString().equalsIgnoreCase("audio"))
                        {
                            isAudio = true;
                        }
                    } else
                    if(tempItem.getFieldName().equalsIgnoreCase("audio_folder"))
                    {
                        audioFolder = tempItem.getString();
                    } else
                    if(tempItem.getFieldName().equalsIgnoreCase("appId"))
                    {
                        appId = tempItem.getString();
                    } else
                    if(tempItem.getFieldName().equalsIgnoreCase("appName"))
                    {
                        appName = tempItem.getString();
                    }
                } else
                {
                    item = tempItem;
                }
            }

            prevrequest = (new StringBuilder("audio?action=edit&sourceAppId=")).append(appId).append("&appName=").append(appName).toString();
            String tempName = item.getName();
            String fileName = tempName.substring(tempName.lastIndexOf("\\") + 1);
            if(isAudio)
            {
                fileToSave = new File((new StringBuilder(String.valueOf(PropertyFileParser.getProperty("TEMP_PATH")))).append(fileName).toString());
                if(!fileToSave.exists())
                {
                    fileToSave.getParentFile().mkdirs();
                }
                out = new FileOutputStream(fileToSave);
                out.write(item.get());
                if(fileToSave.length() > 0L)
                {
                    writeTO(fileToSave, audioFolder, pin, appId, appName, messageBuffer);
                } else
                {
                    messageBuffer.append((new StringBuilder("<font color=\"red\">")).append(fileName.replaceAll("^[\\d|_]*", "")).append(" : ").append("File is empty.").append("</font><br>").toString());
                    logger.info((new StringBuilder(String.valueOf(fileName.replaceAll("^[\\d|_]*", "")))).append(" : ").append("File is empty.").toString());
                }
            }
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if(out != null)
        {
            out.flush();
            out.close();
        }
        if(fileToSave != null)
        {
            fileToSave.delete();
        }
        if(item != null)
        {
            item.delete();
        }
        
        Exception exception;
       
        if(out != null)
        {
            out.flush();
            out.close();
        }
        if(fileToSave != null)
        {
            fileToSave.delete();
        }
        if(item != null)
        {
            item.delete();
        }
       
        if(out != null)
        {
            out.flush();
            out.close();
        }
        if(fileToSave != null)
        {
            fileToSave.delete();
        }
        if(item != null)
        {
            item.delete();
        }
        sendReply(request, response, messageBuffer.toString(), response.encodeURL(prevrequest));
        return;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        String action = request.getParameter("action");
        String sourceAppId = request.getParameter("sourceAppId");
        String audioId = request.getParameter("audioId");
        String appName = request.getParameter("appName");
        if(action != null && action.equalsIgnoreCase("edit") && sourceAppId != null)
        {
            List audioList = DBTools.getAudiosOfApplication(sourceAppId);
            request.setAttribute("audioList", audioList);
            request.setAttribute("audioUploadPane", Boolean.valueOf(true));
            request.getRequestDispatcher("/upload.jsp").forward(request, response);
        } else
        if(action != null && action.equalsIgnoreCase("delete") && validateRequest(audioId, sourceAppId, appName))
        {
            String ids[] = audioId.split(",");
            for(int i = 0; i < ids.length; i++)
            {
                if(!deleteAudioFile(ids[i]))
                {
                    String replyMessage = "Audio file cannot be deleted (File may be in use). Please try later.";
                    String backButton = (new StringBuilder("audio?action=edit&sourceAppId=")).append(sourceAppId).append("&appName=").append(appName).toString();
                    sendReply(request, response, replyMessage, backButton);
                    return;
                }
                DBTools.deleteAudio(ids[i]);
            }

            List audioList = DBTools.getAudiosOfApplication(sourceAppId);
            request.setAttribute("audioList", audioList);
            request.setAttribute("audioUploadPane", Boolean.valueOf(true));
            request.getRequestDispatcher("/upload.jsp").forward(request, response);
        } else
        {
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private boolean validateRequest(String audioId, String sourceAppId, String appName)
    {
        return audioId != null && audioId != "" && sourceAppId != null && sourceAppId != "" && appName != null && appName != "";
    }

    private boolean deleteAudioFile(String audioId)
    {
        AudioVO audioVO = DBTools.getAudioOfById(audioId);
        if(audioVO == null)
        {
            return false;
        }
        File file = new File(audioVO.getRealPath());
        if(file.exists())
        {
            boolean success = file.delete();
            return success;
        } else
        {
            return true;
        }
    }

    private boolean saveToDB(String appId, String path)
        throws SQLException
    {
        if(path != null)
        {
            path = path.replace("\\", "&#92;");
            AudioVO audioVO = new AudioVO();
            audioVO.setAppId(Integer.parseInt(appId));
            audioVO.setPath(path);
            return DBTools.saveAudio(audioVO);
        } else
        {
            return false;
        }
    }

    public String writeTO(File inFile, String outFolder, String pin, String appId, String appName, StringBuffer messageBuffer)
    {
        String path;
        String filePath;
        ZipInputStream in;
        FileInputStream fin;
        path = (new StringBuilder(String.valueOf(outFolder))).append("\\").append(pin).append("\\").append(appName).toString();
        filePath = "";
        in = null;
        fin = null;
        boolean savedToDB = true;
        try
        {
            String name = inFile.getName();
            if(!name.substring(name.lastIndexOf(".") + 1, name.length()).equalsIgnoreCase("zip"))
            {
                fin = new FileInputStream(inFile);
                filePath = saveFile(name, fin, path, pin, messageBuffer);
                 savedToDB = saveToDB(appId, filePath);
                if(filePath != null && !savedToDB)
                {
                    messageBuffer.append((new StringBuilder("<font color=\"green\">")).append(name).append(" : Upload success(replaced duplicate entry).").append("</font> <br>").toString());
                    logger.info((new StringBuilder(String.valueOf(name))).append(":Upload success(replaced duplicate entry)").toString());
                }
            } else
            {
                ZipEntry entry = null;
                in = new ZipInputStream(new FileInputStream(inFile));
                while((entry = in.getNextEntry()) != null) 
                {
                    name = (new File(entry.getName())).getName();
                    filePath = saveFile(name, in, path, pin, messageBuffer);
                     savedToDB = saveToDB(appId, filePath);
                    if(filePath != null && !savedToDB)
                    {
                        messageBuffer.append((new StringBuilder("<font color=\"green\">")).append(name).append(" : Upload success(replaced duplicate entry)").append("</font> <br>").toString());
                        logger.info((new StringBuilder(String.valueOf(name.replaceAll("^[\\d|_]*", "")))).append(" : Upload success(replaced duplicate entry)").toString());
                    }
                }
            }
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
            filePath = "";
        }
        try
        {
            if(in != null)
            {
                in.close();
            }
            if(fin != null)
            {
                fin.close();
            }
            if(inFile != null)
            {
                inFile.delete();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        Exception exception;
     
        try
        {
            if(in != null)
            {
                in.close();
            }
            if(fin != null)
            {
                fin.close();
            }
            if(inFile != null)
            {
                inFile.delete();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
       // throw exception;
        try
        {
            if(in != null)
            {
                in.close();
            }
            if(fin != null)
            {
                fin.close();
            }
            if(inFile != null)
            {
                inFile.delete();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return filePath != null ? filePath.replace("\\", "&#92;") : filePath;
    }

    private String saveFile(String name, InputStream in, String path, String pin, StringBuffer messageBuffer)
        throws IOException
    {
        String filePath;
        FileOutputStream out;
        byte entryBytes[];
        filePath = null;
        out = null;
        if(!isValidAppName(name, pin))
        {
            name = name;
        }
        entryBytes = getEntryBytes(in);
        try
        {
            if(isValidFormat(entryBytes, name))
            {
                File destFile = new File(path, name);
                filePath = destFile.getAbsolutePath();
                if(!destFile.exists())
                {
                    destFile.getParentFile().mkdirs();
                    out = new FileOutputStream(destFile);
                    out.write(entryBytes);
                    messageBuffer.append((new StringBuilder("<font color=\"green\">")).append(name).append(" : Upload success ").append("</font><br>").toString());
                    logger.info((new StringBuilder(String.valueOf(name.replaceAll("^[\\d|_]*", "")))).append(" : Upload success ").toString());
                }
            } else
            {
                messageBuffer.append((new StringBuilder("<font color=\"red\">")).append(name.replaceAll("^[\\d|_]*", "")).append(" : ").append("Invalid encoding of the wave file.  Only CCIT uLaw, 8 bit, Mono, 64kbps are supp" +
"orted"
).append("</font><br>").toString());
                logger.info((new StringBuilder(String.valueOf(name.replaceAll("^[\\d|_]*", "")))).append(" : ").append("Invalid encoding of the wave file.  Only CCIT uLaw, 8 bit, Mono, 64kbps are supp" +
"orted"
).toString());
            }
            
        }
        catch(UnsupportedAudioFileException e)
        {
            messageBuffer.append((new StringBuilder("<font color=\"red\">")).append(name.replaceAll("^[\\d|_]*", "")).append(" : ").append("Upload failed ( ").append(e.getMessage()).append(") </font><br>").toString());
            logger.info((new StringBuilder(String.valueOf(name.replaceAll("^[\\d|_]*", "")))).append(" : ").append("Upload failed ( ").append(e.getMessage()).append(")").toString());
            e.printStackTrace();
        }
        try
        {
            if(out != null)
            {
                out.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        Exception exception;
        
        try
        {
            if(out != null)
            {
                out.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
       
        try
        {
            if(out != null)
            {
                out.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return filePath;
    }

    private boolean isValidAppName(String name, String pin)
    {
        boolean result = true;
        if(name.equals("") || name.indexOf(" ") > -1 || !DBTools.isAdmin(pin) && !name.startsWith(pin))
        {
            result = false;
        }
        return result;
    }

    private void deleteFiles(ArrayList filesToDelete)
    {
        File delFile;
        for(Iterator it = filesToDelete.iterator(); it.hasNext(); deleteDir(delFile))
        {
            String fileName = (String)it.next();
            String audiumHome = DBTools.loadSetting("audium_home");
            delFile = new File((new StringBuilder(String.valueOf(addPathSeparator(audiumHome)))).append(addPathSeparator("applications")).append(fileName).toString());
        }

    }

    private byte[] getEntryBytes(InputStream in)
        throws IOException
    {
        ByteArrayOutputStream entryByteStream = new ByteArrayOutputStream(in.available());
        byte buf[] = new byte[1024];
        int len;
        while((len = in.read(buf)) > 0) 
        {
            entryByteStream.write(buf, 0, len);
        }
        return entryByteStream.toByteArray();
    }

    private String addPathSeparator(String path)
    {
        if(!path.endsWith(File.separator))
        {
            path = (new StringBuilder(String.valueOf(path))).append(File.separator).toString();
        }
        return path;
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

    private boolean isValidFormat(byte entryBytes[], String fileName)
        throws UnsupportedAudioFileException, IOException
    {
        if(entryBytes.length == 0)
        {
            return false;
        }
        AudioFileFormat aff = AudioSystem.getAudioFileFormat(new ByteArrayInputStream(entryBytes));
        AudioFormat af = aff.getFormat();
        return isValidFileExtensio(fileName) && af.getEncoding().equals(javax.sound.sampled.AudioFormat.Encoding.ULAW) && Float.compare(af.getFrameRate(), 8000F) == 0 && af.getChannels() == 1;
    }

    private boolean isValidFileExtensio(String fileName)
    {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        if(fileExtension == null)
        {
            return false;
        }
        String formats[] = VALID_FORMATS.split(",");
        for(int i = 0; i < formats.length; i++)
        {
            if(fileExtension.equalsIgnoreCase(formats[i]))
            {
                return true;
            }
        }

        return false;
    }

    private void sendReply(HttpServletRequest request, HttpServletResponse response, String replyMessage, String backButton)
        throws IOException, ServletException
    {
        request.setAttribute("deploy", replyMessage);
        request.setAttribute("back", backButton);
        request.setAttribute("messagePane", Boolean.valueOf(true));
        request.getRequestDispatcher("/upload.jsp").forward(request, response);
    }

    public static void main(String args[])
        throws Exception
    {
        AudioServlet ser = new AudioServlet();
        ser.writeTO(new File("c://songs.zip"), "c://test", "123", "1", "tmp", new StringBuffer());
        InputStream in = new FileInputStream(new File("c://start.wav"));
        ser.saveFile("start.wav", in, "c://audio", "5120", new StringBuffer());
    }

}
