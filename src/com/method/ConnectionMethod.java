package com.method;

import com.SMTPCommunication;

import java.util.List;

/**
 * Created by kifkif on 16/02/2017.
 */
public class ConnectionMethod extends MethodCommand
{
    private static int tryLimit = 3;

    private int tryNumber;

    public ConnectionMethod() {
        super("APOP");
    }

    @Override
    protected String onError() {
        return "Error connection";
    }

    @Override
    public boolean processCommand(List<String> lines)
    {

        return false;
    }
}
