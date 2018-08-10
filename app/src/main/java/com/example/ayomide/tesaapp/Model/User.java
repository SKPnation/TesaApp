package com.example.ayomide.tesaapp.Model;

public class User {

    private String email;
    private String password;

    public User() {
    }

    public User(String Pemail, String Ppassword) {

        email = Pemail;
        password = Ppassword;
    }

    public String setEmail(String Pemail) {
        email = Pemail;
        return email;
    }

    public String getEmail() {

        return email;
    }

    public String getpassword() {

        return password;
    }
}

