package com.method;

import com.SMTPCommunication;

import java.io.IOException;
import java.util.List;

/**
 * Created by kifkif on 16/02/2017.
 */
public class ConnectionMethod extends SMTPMethod
{
    private static int tryLimit = 3;

    private int tryNumber;

    public ConnectionMethod() {
        super("APOP");
        tryNumber = 0;
    }

//    @Override
//    protected String onError() {
//        return "Error connection";
//    }

    @Override
    public boolean processCommand(List<String> lines)
    {
        if(communication.isConnected() || tryNumber >= tryLimit)
            return false;
        tryNumber++;
        for(String line : lines)
        {
            if(line.toUpperCase().contains("APOP"))
            {
                String[] apop = line.split(" ");
                if(apop.length != 2)
                    return false;
                String name = apop[1];
                if(name.isEmpty())
                    return false;
                try {
                    sendOK("maildrop has 1 message (369 octets)");
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                communication.clientConnected(name);
                return true;
            }
        }
        
        log(lines.get(0));
        return false;
    }
}
