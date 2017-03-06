package com.stockage;

import com.Server;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.FileReader;
import java.util.ArrayList;
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
         parser = new JSONParser();
    }

    public void setServer(Server server)
    {
        this.server = server;
    }

    public UserBank getUserBank()
    {
        UserBank bank = new UserBank();
        try {

            Object obj = parser.parse(new FileReader(server.getRootDir()));

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray userList = (JSONArray) jsonObject.get("users");
            for (JSONObject jsonUser : (Iterable<JSONObject>) userList) {
                String name = (String) jsonUser.get("name");
                String control = (String) jsonUser.get("control");
                JSONArray messageList = (JSONArray) jsonUser.get("messages");
                List<String> messages = new ArrayList<>();
                for (String message : (Iterable<String>) messageList) {
                    messages.add(message);
                }

                User user = new User(name, control, messages);
                bank.addUser(user);
                System.out.println(name+"|"+control+"|"+messages.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bank;
    }
}
