package com.example.walun.fireapp3;

/**
 * Created by walun on 09-07-2017.
 */


public class Person {
    //name and address string
    private String name;
    private String email;
    private String password;




    public Person() {
      /*Blank default constructor essential for Firebase*/
    }
    //Getters and setters

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {  this.email = email;  }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

}
