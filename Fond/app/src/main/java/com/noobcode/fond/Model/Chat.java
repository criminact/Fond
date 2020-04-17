package com.noobcode.fond.Model;

import java.util.List;

public class Chat {

    String profile;
    String name;
    String id;

    public Chat(String profile, String name, String id) {
        this.profile = profile;
        this.name = name;
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
