package com.example.mansi.busezon;

/**
 * Created by Shriya on 03-04-2018.
 */

public class UserInformation {
    public String name,address,email,phoneno,password;

    public UserInformation(){};

    public UserInformation(String name, String address, String email, String phoneno, String password) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneno = phoneno;
        this.password = password;
    }
}