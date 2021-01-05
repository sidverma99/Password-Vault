package com.example.vault;

public class Data {
    private int id;
    private String website;
    private String email;
    private String password;

    public static final String TABLE_NAME="myData";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_WEB="link";
    public static final String COLUMN_USERNAME="username";
    public static final String COLUMN_PASSWORD="password";

    public static final String CREATE_TABLE="create table "+TABLE_NAME+"("+COLUMN_ID+" INTEGER PRIMARY KEY AUTO INCREMENT,"+COLUMN_WEB+" TEXT,"+COLUMN_USERNAME+" TEXT,"+COLUMN_PASSWORD+" TEXT"+")";

    public Data() {
    }

    public Data(int id, String website, String email, String password) {
        this.id = id;
        this.website = website;
        this.email = email;
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
