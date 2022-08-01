package bgu.spl.net.BGS;

import bgu.spl.net.BGS.Messages.BGSDate;



public class BGSUser {
    private BGSDate birthday;
    private String username;
    private String password;
    private int connectionId;
    private boolean isLogged;
    public int numPosts;
    public int numFollowing;
    public BGSUser(String name, String pass, BGSDate birthday){
        username=name;password=pass;isLogged=false;this.birthday=birthday;
        numPosts =0;
        numFollowing =0;
    }
    public boolean isLogged(){return isLogged;}
    public boolean Login(String name,String pass){
        if(isLogged){
            //System.out.println("isLogged");
            return false;
        }
        if(!name.equals(username) | !pass.equals(password)){
            //System.out.println(name+","+username);
            //System.out.println(pass+","+password);
            return false;
        }
        isLogged=true;
        return true;
    }
    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }
    public int getConnectionId(){return connectionId;}
    public String getUsername() {
        return username;
    }
    public int getAge(){
        return birthday.getAge();
    }
    public void logOut() {
        isLogged = false;
    }

}
