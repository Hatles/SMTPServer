package com.stockage;

import com.Server;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kifkif on 28/02/2017.
 */
public class Stockage
{
    private static Stockage instance;

    public static synchronized Stockage getInstance()
    {
        if(instance == null)
            instance = new Stockage();

        return instance;
    }

    protected Server server;
    protected JSONParser parser;

    protected Stockage()
    {
        JSONParser parser = new JSONParser();
    }

    public void setServer(Server server)
    {
        this.server = server;
    }

    public JSONObject getDatas()
    {
        try {

            Object obj = parser.parse(new FileReader(server.getRootDir()));

            JSONObject jsonObject = (JSONObject) obj;

            String name = (String) jsonObject.get("name");
            String control = (String) jsonObject.get("control");
            JSONArray messageList = (JSONArray) jsonObject.get("messages");
            List<String> messages = new ArrayList<>();
            for (String message : (Iterable<String>) messageList) {
                messages.add(message);
            }

            User user = new User(name, control, messages);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
