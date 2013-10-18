package com.audiumcorp.webportal.admin;

import java.io.Serializable;

public class AudioVO
    implements Serializable
{

    private int id;
    private int appId;
    private String path;
    private String description;

    public AudioVO()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getAppId()
    {
        return appId;
    }

    public void setAppId(int appId)
    {
        this.appId = appId;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getRealPath()
    {
        if(path == null)
        {
            return "null";
        } else
        {
            return path.replace("&#92;", "/");
        }
    }
}
