package com.example.mansi.busezon;

/**
 * Created by Shriya on 05-06-2018.
 */

public class FirebaseUserInformation
{
    public String name,address,email,phoneno,password;


    public FirebaseUserInformation(){};

    public FirebaseUserInformation(String name, String address, String email, String phoneno, String password) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneno = phoneno;
        this.password = password;
    }
}
