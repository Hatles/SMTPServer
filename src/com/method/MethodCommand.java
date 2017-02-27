package com.method;

import java.util.List;

/**
 * Created by kifkif on 16/02/2017.
 */
public abstract class MethodCommand extends Method
{
    protected String command;

    public MethodCommand(String command) {
        super();
        this.command = command.toUpperCase();
    }

    public String getCommand() {
        return command;
    }

    public boolean process(String command, List<String> lines)
    {
        if(command.toUpperCase().equals(this.command))
        {
            if(!this.processCommand(lines))
                log("error process command");
            return true;
        }
        else
            return false;
    }

    public abstract boolean processCommand(List<String> lines);
}
