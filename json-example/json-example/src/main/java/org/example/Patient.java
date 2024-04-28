package org.example;

public class Patient {
    private String name;
    private String sureName;
    private String address;
    private User user;

    public Patient(String name, String sureName, String address, User user) {
        this.name = name;
        this.sureName = sureName;
        this.address = address;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSureName() {
        return sureName;
    }

    public void setSureName(String sureName) {
        this.sureName = sureName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", sureName='" + sureName + '\'' +
                ", address='" + address + '\'' +
                ", user=" + user +
                '}';
    }
}
