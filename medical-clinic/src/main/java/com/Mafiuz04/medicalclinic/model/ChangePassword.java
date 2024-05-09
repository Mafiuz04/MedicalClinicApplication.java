package com.Mafiuz04.medicalclinic.model;


import java.util.Objects;

public class ChangePassword {
    private String password;

    public ChangePassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangePassword that = (ChangePassword) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    @Override
    public String toString() {
        return "ChangePassword{" +
                "password='" + password + '\'' +
                '}';
    }
}
