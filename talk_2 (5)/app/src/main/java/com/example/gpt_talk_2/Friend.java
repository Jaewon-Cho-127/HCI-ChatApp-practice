package com.example.gpt_talk_2;

public class Friend {
    private String name;
    private String message;

    public Friend(String name, String email) {
        this.name = name;
        this.message = email;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}