package com.example.parkinginfoapp;

import java.util.ArrayList;

public class User {
    String email;
    String firstName;
    String lastName;
    ArrayList<String> permits;
    String type;

    public User() {

    }

    public User(String email, String firstName, String lastName, ArrayList<String> permits, String type) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.permits = permits;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ArrayList<String> getPermits() {
        return permits;
    }

    public void setPermits(ArrayList<String> permits) {
        this.permits = permits;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
