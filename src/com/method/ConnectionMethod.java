package com.method;

import com.SMTPCommunication;
import com.stockage.Stockage;
import com.stockage.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
                    User user = Stockage.getInstance().getUserBank().getUser(name);
                    List<String> messages = user.getMessages();
                    int length = 0;
                    for(String message : messages){
                        length+=message.getBytes(StandardCharsets.UTF_8).length;
                    }
                    sendOK("maildrop has " + messages.size() +" message ("+ length +" octets)");
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } catch (Exception e){

                }

                communication.clientConnected(name);
                return true;
            }
        }
        
        log(lines.get(0));
        return false;
    }
}
