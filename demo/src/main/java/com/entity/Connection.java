package com.entity;

public class Connection {
    private int id;
    private String host;
    private int port;
    private String username;
    private String password;

    //setters
    public void setId(int id){
        this.id = id;
    }
    public void setHost(String host){
        this.host = host;
    }
    public void setPort(int port){
        this.port = port;
    }
    public void setUserName(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }

    //getters
    public int getId(){
        return id;
    }
    public String getHost(){
        return host;
    }
    public int getPort(){
        return port;
    }
    public String getUserName(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}
