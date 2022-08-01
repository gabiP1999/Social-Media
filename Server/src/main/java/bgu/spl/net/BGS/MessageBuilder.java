package bgu.spl.net.BGS;

import bgu.spl.net.BGS.Messages.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public  class MessageBuilder {
    private static final short Register = 1;
    private static final short Login = 2;
    private static final short Logout = 3;
    private static final short Follow = 4;
    private static final short Post = 5;
    private static final short PM = 6;
    private static final short LogStat = 7;
    private static final short Stat = 8;
    private static final short Block = 12;

    public static BaseMessage Build(short Opcode, List<String> content,List<String> tagged,byte follow){
        //System.out.println("build");
        //System.out.println("follow:"+follow);
        if(Opcode==Register)return BuildRegister(content);
        if(Opcode==Login)return BuildLogin(content);
        if(Opcode==Logout)return BuildLogout();
        if(Opcode==Follow)return BuildFollow(content,follow);
        if(Opcode==Post)return BuildPost(content,tagged);
        if(Opcode==PM)return BuildPrivateMessage(content);
        if(Opcode==LogStat)return BuildLogStat();
        if(Opcode==Stat)return BuildStat(content);
        if(Opcode==Block)return BuildBlock(content);
        return null;
    }

    private static BlockMessage BuildBlock(List<String> content) {
        BlockMessage error = new BlockMessage(content);
        if(content.isEmpty()){
            error.setValid(false);
            return error;
        }
        return new BlockMessage(content);
    }

    private static StatusMessage BuildStat(List<String> content) {
        System.out.println(content.get(0));
        StatusMessage error = new StatusMessage(new ArrayList<>());
        error.setValid(false);
        if(content.size()!=1){
            return error;
        }
        else{
            String users = content.get(0);
            List<String> user_list = splitStat(users);
            return new StatusMessage(user_list);
        }

    }

    private static List<String> splitStat(String users) {
        List<String> ans = new ArrayList<>();
        char del = '|';
        while(users.length()>0) {
            int index = users.indexOf(del);
            if(index ==-1){
                ans.add(users);
                //System.out.println("SplitStat::"+users);
                break;
            }
            String toAdd = users.substring(0,index);
            //System.out.println("SplitStat::"+toAdd);
            ans.add(toAdd);
            users=users.substring(index+1);
        }
        return ans;
    }

    private static LogStatMessage BuildLogStat() {
        return new LogStatMessage();
    }

    private static PrivateMessage BuildPrivateMessage(List<String> content) {
        PrivateMessage error=new PrivateMessage(new ArrayList<>(),"");
        error.setValid(false);
        if(content.isEmpty())return error;
        String sendTo= content.remove(0);
        return new PrivateMessage(content,sendTo);
    }

    private static PostMessage BuildPost(List<String> content, List<String> tagged) {
        List<String> real_tagged=findTagged(content.get(0));
        return new PostMessage(content,real_tagged);
    }

    private static List<String> findTagged(String s) {
        String[] split = s.split(" ");
        List<String> res = new ArrayList<>();
        for(String a : split){
            if(a.charAt(0)=='@')res.add(a.substring(1));
        }
        return res;
    }

    private static FollowMessage BuildFollow(List<String> content,byte follow) {
        //System.out.println("buildFollow");
        FollowMessage error = new FollowMessage("");
        error.setValid(false);
        if(content.size()<1) return error;
        String toFollow = content.get(0);
        FollowMessage ans = new FollowMessage(toFollow);
        if(follow==1){
            ans=new FollowMessage(toFollow.substring(1));
            ans.setUnfollow();
        }
        return ans;
    }

    private static LogoutMessage BuildLogout() {
        return new LogoutMessage();
    }

    private static LoginMessage BuildLogin(List<String> content) {
        LoginMessage error = new LoginMessage("","",(byte)0);
        error.setValid(false);
        if(content.size()!=3){
            System.out.println("Error:BuildLog::content!=3");
            return error;
        }
        byte chapta = (byte)content.get(2).charAt(0);
        if(chapta!=0 & chapta!=1)return error;
        return new LoginMessage(content.get(0), content.get(1),chapta );
    }

    private static RegisterMessage BuildRegister(List<String> content) {
        RegisterMessage error = new RegisterMessage("","",null);
        error.setValid(false);
        if(content.size()!=3){
            System.out.println("Error:BuildReg::content!=3");
            return error;
        }

        String[] date_string = content.get(2).split("-");
        try{
            BGSDate date = new BGSDate(Integer.parseInt(date_string[0]),
                    Integer.parseInt(date_string[1]),
                    Integer.parseInt(date_string[2]));
            return new RegisterMessage(content.get(0), content.get(1) ,date);
        }
        catch (Exception e){
            return error;
        }
    }

   

}
