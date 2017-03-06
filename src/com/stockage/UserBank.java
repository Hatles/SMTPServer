package com.stockage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kifkif on 28/02/2017.
 */
public class UserBank {
    private List<User> users;

    public UserBank()
    {
        users = new ArrayList<>();
    }

    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user)
    {
        this.users.add(user);
    }

    public User getUser(String name) throws Exception {
        for(User user : users)
        {
            if(user.getName().equals(name))
                return user;
        }
        throw new Exception("User "+name+" doesn't have account.");
    }
}
