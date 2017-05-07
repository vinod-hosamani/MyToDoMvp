package com.bridgelabz.mytodomvp.registration.model;

/**
 * Created by bridgeit on 7/5/17.
 */
public class UserModel {
    private String id;
    private String mobile;
    private String password;
    private String fullname;
    private String email;

    public UserModel()
    {

    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
