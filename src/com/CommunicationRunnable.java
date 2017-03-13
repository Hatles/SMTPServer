package com;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kifkif on 15/02/2017.
 */
abstract class CommunicationRunnable extends ServerRunnable implements Communication
{
    private static int count = 0;

    private Socket socket;

    private BufferedReader in;
    private DataOutputStream out;

    private boolean firstLoop;

    int id;

    public CommunicationRunnable(Server server, Socket socket)
    {
        super(server);
        this.socket = socket;
        id = count++;
        this.initBuffers();
        firstLoop = true;
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
            close();
        }
    }

    @Override
    protected void loop()
    {
        if(firstLoop)
        {
            onStart();
            firstLoop = false;
        }

        try
        {
            String line;
            List<String> lines = new ArrayList<String>();

            log("waiting message");
            boolean reading = true;
            while(reading)
            {
                line = in.readLine();

                if(line == null)
                {
                    reading = false;
                    this.stop();
                }
                else if(line.length() > 0)
                {
                    lines.add(line);
                }
                else
                    reading = false;
            }

            if(lines.size() > 0)
            {
                log("message received : "+lines.get(0)+"...");
                onClientCommunication(lines);
            }

        } catch (IOException e)
        {
            //e.printStackTrace();
            log(e.getMessage());
            this.stop();
        }
    }

    protected abstract void onStart();

    protected abstract void onClientCommunication(List<String> lines);

    public void log(String m)
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

    public void close()
    {
        log("closing");
        this.onClose();
        try
        {
            socket.close();
        } catch (IOException e)
        {
            log("Error during closing : "+e.getMessage());
            e.printStackTrace();
        }
        server.removeClient(this);
        log("closed");
    }

    protected abstract void onClose();

    public void writeData(File file) throws IOException
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

    public void writeLine(String message) throws IOException
    {
        write(message+"\r\n");//CR LF
    }

    public void write(String message) throws IOException
    {
        out.writeBytes(message);
    }

    public void send(String message) throws IOException
    {
        if(message != null && !message.equals(""))
            writeLine(message);
        writeLine("");
        out.flush();
        log("sending message");
    }

    public void send() throws IOException
    {
        send("");
    }
}
