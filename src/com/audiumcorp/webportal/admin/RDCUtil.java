package com.audiumcorp.webportal.admin;

import java.io.*;
import java.net.InetAddress;
import java.util.Properties;

// Referenced classes of package com.audiumcorp.webportal.admin:
//            DBToolsRDC

public final class RDCUtil
{

    public RDCUtil()
    {
    }

    public static void main(String args[])
    {
        if(args.length == 0)
        {
            System.exit(1);
        }
        try
        {
            Properties properties = new Properties();
            properties.load(new FileInputStream("C:/WINDOWS/SYSVOL/domain/scripts/rdc.properties"));
            String backup_dir = (String)properties.get("backup_dir");
            String copy_from_workspace_dir = (String)properties.get("copy_from_workspace_dir");
            String copy_to_workspace_dir = (String)properties.get("copy_to_workspace_dir");
            backup_dir = (new StringBuilder(String.valueOf(backup_dir))).append("/workspace").append(System.currentTimeMillis()).toString();
            copyDirectory(new File(copy_to_workspace_dir), new File(backup_dir));
            copyDirectory(new File(copy_from_workspace_dir), new File(copy_to_workspace_dir));
            InetAddress addr = InetAddress.getLocalHost();
            String ipAddress = addr.getHostAddress();
            for(int i = 0; i < 2; i++)
            {
                System.out.println(ipAddress);
                System.out.println(properties.get(ipAddress));
            }

            DBToolsRDC.updateRDPConfig((new Integer(args[0])).intValue(), (String)properties.get(ipAddress));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void copyFile(File src, File dst)
        throws IOException
    {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte buf[] = new byte[1024];
        int len;
        while((len = in.read(buf)) > 0) 
        {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void copyDirectory(File srcDir, File dstDir)
        throws IOException
    {
        if(srcDir.isDirectory())
        {
            if(!dstDir.exists())
            {
                dstDir.mkdir();
            }
            String children[] = srcDir.list();
            for(int i = 0; i < children.length; i++)
            {
                copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]));
            }

        } else
        {
            copyFile(srcDir, dstDir);
        }
    }
}
