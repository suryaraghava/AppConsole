package com.audiumcorp.webportal.admin;


public class RDCConfig
{

    public static final int NOT_IN_USE = 0;
    public static final int IN_USE = 1;
    private int id;
    private String name;
    private String ipAddress;
    private boolean inUse;

    public RDCConfig()
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

    public boolean isInUse()
    {
        return inUse;
    }

    public void setInUse(boolean inUse)
    {
        this.inUse = inUse;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
