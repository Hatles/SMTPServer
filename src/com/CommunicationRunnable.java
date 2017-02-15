package com;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kifkif on 15/02/2017.
 */
abstract class CommunicationRunnable extends ServerRunnable
{
    private static int count = 0;

    private Socket socket;

    private BufferedReader in;
    private DataOutputStream out;

    int id;

    public CommunicationRunnable(Server server, Socket socket)
    {
        super(server);
        this.socket = socket;
        id = count++;
        this.initBuffers();
    }

    public void initBuffers()
    {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e)
        {
            e.printStackTrace();
            log(e.getMessage());
            onClose();
        }
    }

    @Override
    protected void loop()
    {
        try
        {
            String line;
            List<String> lines = new ArrayList<String>();

            while((line = in.readLine())!=null && line.length() > 0)
            {
                lines.add(line);
            }

            if(lines.size() > 0)
                onClientCommunication(lines);

        } catch (IOException e)
        {
            //e.printStackTrace();
            log(e.getMessage());
        }
    }

    protected abstract void onClientCommunication(List<String> lines);

    protected void log(String m)
    {
        server.log(this.getTag() +" : "+ m);
    }

    public int getID()
    {
        return id;
    }

    @Override
    protected String getTag()
    {
        return "[Client-"+id+"]";
    }

    public void onClose()
    {
        log("closing");
        this.closing();
        try
        {
            socket.close();
        } catch (IOException e)
        {
            log("Error during closing : "+e.getMessage());
            e.printStackTrace();
        }
        server.removeClient(this);
        this.stop();
    }

    protected abstract void closing();

    protected void writeData(File file) throws IOException
    {
        log("Start log data");
        FileInputStream fea;
        try
        {
            fea = new FileInputStream(file);
            int content;
            while((content = fea.read()) != -1)
            {
                out.writeByte(content);
            }
            fea.close();
            //out.writeByte(-1);
        } catch (FileNotFoundException e)
        {
            log(e.getMessage());
            e.printStackTrace();
        }
        log("End log data");
    }

    protected void writeLine(String message) throws IOException
    {
        write(message+"\r\n");//CR LF
    }

    protected void write(String message) throws IOException
    {
        out.writeBytes(message);
    }

    protected void send(String message) throws IOException
    {
        writeLine(message);
        out.flush();
        log("sending message");
    }

    protected void send() throws IOException
    {
        send("");
    }
}
