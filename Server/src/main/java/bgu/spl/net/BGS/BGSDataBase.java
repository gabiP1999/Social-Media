package bgu.spl.net.BGS;

import bgu.spl.net.BGS.Messages.BGSDate;
import bgu.spl.net.BGS.Messages.BaseMessage;
import bgu.spl.net.BGS.Messages.LogStatObj;
import bgu.spl.net.BGS.Messages.NotificationMessage;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BGSDataBase {
    private Map<String,BGSUser> Users;
    private Map<String, List<String>> Followers;
    private Map<String,List<BaseMessage>> Messages;
    private Map<String,List<String>> Blocked;
    private Map<String, Queue<BaseMessage>> Missed;
    private static BGSDataBase instance;
    private static String[] filtered = {"Bibi","Nutella","Pokemon"};
    private static String filter_word = "<filtered>";

    private BGSDataBase(){
        Blocked=new ConcurrentHashMap<>();
        Missed= new ConcurrentHashMap<>();
        Users=new ConcurrentHashMap<>();
        Followers=new ConcurrentHashMap<>();
        Messages=new ConcurrentHashMap<>();
    }

    public static BGSDataBase getInstance() {
        if(instance==null){
            instance=new BGSDataBase();
        }
        return instance;
    }

    public boolean register (String username, String password, BGSDate birthday){
        if(isFiltered(username))return false;
        if(Users.containsKey(username))return false;
        Users.put(username,new BGSUser(username,password,birthday));
        Followers.put(username,new ArrayList<>());
        Messages.put(username,new ArrayList<>());
        Missed.put(username,new ArrayDeque<>());
        Blocked.put(username,new ArrayList<>());
        return true;
    }

    private boolean isFiltered(String username) {
        for (String s : filtered) {
            if (username.equals(s)) return true;
        }
        return false;
    }

    public boolean block(String user, String toBlock){
        if((!Users.containsKey(toBlock))| (!Users.containsKey(user)))return false;
        if(!Users.get(user).isLogged()) return false;
        unfollow(user,toBlock);
        unfollow(toBlock,user);
        Blocked.get(user).add(toBlock);
        Blocked.get(toBlock).add(user);
        return true;
    }

    public boolean unfollow(String user, String toUnfollow) {
        if((!Users.containsKey(toUnfollow))){
            //System.out.println("MARK VI '"+toUnfollow+"'");
            return false;
        }
        if(!Users.containsKey(user)){
            //System.out.println("MARK IV");
            return false;
        }
        if(!Users.get(user).isLogged()){
            //System.out.println("MARK V");
            return false;
        }
        if(!Followers.get(toUnfollow).contains(user)){
            return false;
        }
        Followers.get(toUnfollow).remove(user);
        return true;
    }
    public boolean follow(String user,String toFollow){
        if((!Users.containsKey(toFollow))| (!Users.containsKey(user)))return false;
        if(!Users.get(user).isLogged()) return false;
        if(Followers.get(toFollow).contains(user))return false;
        Followers.get(toFollow).add(user);
        Users.get(user).numFollowing++;
        return true;
    }
    public boolean follow(String user,List<String> toFollow){
        if(!Users.containsKey(user))return false;
        if(!Users.get(user).isLogged()) return false;
        boolean atLeastOne=false;
        for(String toF : toFollow){
            if(follow(user,toF))atLeastOne=true;
        }
        return atLeastOne;
    }
    public boolean unfollow(String user,List<String> toUnfollow){
        if(!Users.get(user).isLogged()) return false;
        boolean atLeastOne=false;
        for(String toF : toUnfollow){
            if(unfollow(user,toF))atLeastOne=true;
        }
        return atLeastOne;
    }
    public BGSUser login(String user,String pass){
        if(!Users.containsKey(user))return null;
        if(Users.get(user).isLogged()) return null;
        if(Users.get(user).Login(user, pass)){
            return Users.get(user);
        }
        return null;
    }
    public boolean block(String user,List<String> toBlock){
        //System.out.println("DATABASE::Block");
        if(!Users.containsKey(user))return false;
        if(!Users.get(user).isLogged()) return false;
        return block(user,toBlock.get(0));
    }
    public String[] getFiltered(){return filtered;}

    public boolean postMessage(NotificationMessage msg, String username) {
        if(!Users.containsKey(username))return false;
        if(!Users.get(username).isLogged()) return false;
        Users.get(msg.getSender()).numPosts++;
        Messages.get(username).add(msg); return true;
    }

    public List<BGSUser> getUsers(List<String> users) {
        List<BGSUser> listUsers= new ArrayList<>();
        for (String s :users)
        {
            BGSUser u = Users.get(s);
            if(u==null)return null;
            listUsers.add(u);
        }
        return listUsers;
    }

    public List<BGSUser> getFollowers(String username) {
        List<BGSUser> listUsers= new ArrayList<>();
        for (String s :Followers.get(username))
        {
            listUsers.add(Users.get(s));
        }
        return listUsers;
    }

    public List<LogStatObj> logstat(String username) {
        if(!Users.containsKey(username))return null;
        if(!Users.get(username).isLogged())return null;
        List<LogStatObj> LogStat = new ArrayList<>();
        for (BGSUser user : Users.values())
        {
            if(user.isLogged())
                LogStat.add(new LogStatObj((short) user.getAge(),(short) user.numPosts,(short) this.getFollowers(user.getUsername()).size(),(short)user.numFollowing ));
        }
        return LogStat;
    }

    public boolean isBlocked(String user1, String user2) {
        return Blocked.get(user1).contains(user2);
    }

    public boolean logout(String username) {
        if(!Users.containsKey(username))return false;
        if(!Users.get(username).isLogged()) return false;
        Users.get(username).setConnectionId(-1);
        Users.get(username).logOut(); return true;
    }

    public Queue<BaseMessage> getMissedMessages(String username) {
        Queue<BaseMessage> queue =  new ArrayDeque<>(Missed.get(username)) ;
        Missed.get(username).clear();
        return queue;
    }

    public boolean addToMissedMesseges(String username, NotificationMessage msg) {
        if(!Users.containsKey(username))return false;

        Missed.get(username).add(msg);
        return true;
    }

    public boolean privateMessage(NotificationMessage msg ,String username)
    {
        if(!Users.containsKey(username))return false;
        if(!Users.get(username).isLogged())return false;
        if(isBlocked(username, msg.getSender()))return false;
        Messages.get(username).add(msg);
        return true;
    }

    public BGSUser getUser(String user) {
        return Users.get(user);
    }

    public List<LogStatObj> status(String username, List<String> users) {
        //System.out.println("USERS:");
        for(String s : users){
            //System.out.println(s);
        }
        if(!Users.containsKey(username)){
            //System.out.println("MARK I");
            return null;
        }
        if(!Users.get(username).isLogged()){
            //System.out.println("MARK II");
            return null;
        }
        List<BGSUser> bgsUsers = getUsers(users);
        if(bgsUsers==null){
            //System.out.println("MARK III");
            return null;
        }
        List<LogStatObj> LogStat = new ArrayList<>();
        for (BGSUser user : bgsUsers)
        {
            LogStat.add(new LogStatObj((short)user.getAge(),(short) user.numPosts,(short) this.getFollowers(user.getUsername()).size(),(short)user.numFollowing ));
        }
        return LogStat;
    }
}
