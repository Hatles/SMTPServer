package com.stockage;

import java.util.List;

/**
 * Created by kifkif on 28/02/2017.
 */
public class User
{
    private String  name;
    private String control;
    private List<String> messages;

    public User(String name, String control, List<String> messages)
    {
        this.name = name;
        this.control = control;
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public String getMessage(int i)
    {
        return this.messages.get(i-1);
    }
}
