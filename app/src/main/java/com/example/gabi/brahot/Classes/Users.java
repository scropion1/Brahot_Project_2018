package com.example.gabi.brahot.Classes;

/**
 * Created by Gabi on 2/25/2018.
 */

public class Users {

    private String name, lastName, city, email;

    public Users(String name, String lastName, String city, String email){
        this.name = name;
        this.lastName = lastName;
        this.city = city;
        this.email = email;
    }

    public Users(){

    }

    public String getName() {
        return name;
    }

    public String getLast() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
