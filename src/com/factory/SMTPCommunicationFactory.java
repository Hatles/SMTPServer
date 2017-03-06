package com.factory;

import com.SMTPCommunication;
import com.method.ConnectionMethod;
import com.method.Method;
import com.method.QuitMethod;

import java.util.List;

/**
 * Created by kifkif on 16/02/2017.
 */
public class SMTPCommunicationFactory implements CommunicationFactory {

    @Override
    public void buildCommunication(SMTPCommunication communication)
    {
        communication.setConnectionMethod(new ConnectionMethod());
        communication.addMethod(new QuitMethod());
//        communication.addMethod(new ConnectionMethod());
    }
}
