package com.method;
import java.io.IOException;
import java.util.List;

/**
 * Created by corentinmarechal on 06/03/2017.
 */
public class RetrMethod extends SMTPMethod {

    public RetrMethod() {
        super("RETR");
    }

    @Override
    public boolean processCommand(List<String> lines) {
        if (communication.getName().isEmpty())
            return false;


        return false;
    }
}
