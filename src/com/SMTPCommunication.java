package com;

import java.net.Socket;
import java.util.List;

/**
 * Created by kifkif on 15/02/2017.
 */
public class SMTPCommunication extends CommunicationRunnable
{
    private String name;

    public SMTPCommunication(Server server, Socket socket) {
        super(server, socket);
    }

    @Override
    protected void onClientCommunication(List<String> lines)
    {

    }

    @Override
    protected void closing()
    {

    }

    protected String getTag()
    {
        return "[Client-"+id+"-"+name+"]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
