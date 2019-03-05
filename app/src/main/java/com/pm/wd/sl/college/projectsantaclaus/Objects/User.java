package com.pm.wd.sl.college.projectsantaclaus.Objects;

public class User {
    String uid;
    String first_name;
    String last_name;
    String email;
    String phone;
    String reg_date;
    String reg_time;

    public User(String uid, String first_name, String last_name, String email, String phone, String reg_date, String reg_time) {
        this.uid = uid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.reg_date = reg_date;
        this.reg_time = reg_time;
    }

    public String getRegSate() {
        return reg_date;
    }

    public User setRegSate(String reg_date) {
        this.reg_date = reg_date;
        return this;
    }

    public String getRegTime() {
        return reg_time;
    }

    public User setRegTime(String reg_time) {
        this.reg_time = reg_time;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public User setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getFirstName() {
        return first_name;
    }

    public User setFirstName(String first_name) {
        this.first_name = first_name;
        return this;
    }

    public String getLastName() {
        return last_name;
    }

    public User setLastName(String last_name) {
        this.last_name = last_name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}
