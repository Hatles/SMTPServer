package com.method;

import com.SMTPCommunication;
import com.Utils;
import com.stockage.Message;
import com.stockage.Stockage;
import com.stockage.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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
        if(communication.isConnected())
            return false;
        if(tryNumber >=tryLimit) {
            try {
                sendERR("authentification failure server signing off");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            communication.close();
            return false;
        }

        tryNumber++;
        for(String line : lines)
        {
            if(line.toUpperCase().contains("APOP"))
            {
                String[] apop = line.split(" ");
                if(apop.length != 3) {
                    try {
                        sendERR("authentification failure more than "+ tryNumber +" try");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    return false;
                }
                String name = apop[1];
                String control = apop[2];
                if(name.isEmpty() || control.isEmpty()){
                    try {
                        sendERR("authentification failure more than "+ tryNumber +" try");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    return false;
                }

                try {
                    User user = Stockage.getInstance().getUserBank().getUser(name);
                    byte[] controlCrypt = MessageDigest.getInstance("MD5").digest(this.communication.getTimestamp().concat(user.getControl()).getBytes());
                    if(!bytesToHex(controlCrypt).toLowerCase().equals(control)){
                        try {
                            sendERR("authentification failure more than "+ tryNumber +" try");
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                        return false;
                    }

                    List<Message> messages = user.getMessages();
                    int length = 0;
                    for(Message message : messages){
                        length+= Utils.buildMessage(message).getBytes(StandardCharsets.UTF_8).length;
                    }
                    communication.clientConnected(name);
                    sendOK("maildrop has " + messages.size() +" message(s) ("+ length +" octets)");
                } catch (Exception e){
                    try {
                        sendERR("authentification failure more than "+ tryNumber +" try");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    return false;
                }

                return true;
            }
        }
        
        log(lines.get(0));
        return false;
    }

    public  String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
