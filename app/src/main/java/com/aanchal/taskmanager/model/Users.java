package com.aanchal.taskmanager.model;

public class Users {

    private  String firstName;
    private  String secondName;
    private  String username;
    private  String password;
    private  String image;

    public Users(String firstName, String secondName, String username, String password, String image) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.username = username;
        this.password = password;
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
