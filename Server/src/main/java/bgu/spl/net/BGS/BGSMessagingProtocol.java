package bgu.spl.net.BGS;

import bgu.spl.net.BGS.Messages.*;
import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.api.Connections;
import java.util.List;
import java.util.Queue;

public class BGSMessagingProtocol implements BidiMessagingProtocol<BaseMessage> {
    private BGSDataBase dataBase;
    private int connectionId;
    private boolean shouldTerminate;
    private BGSConnections Connections;
    public BGSMessagingProtocol(){
        dataBase=BGSDataBase.getInstance();
        shouldTerminate=false;
    }
    @Override
    public void start(int connectionId, Connections<BaseMessage> connections) {
        this.connectionId=connectionId;
        this.Connections = (BGSConnections) connections;
    }

    @Override
    public void process(BaseMessage message) {
        if(message.isValid())
            message.visit(this);
        else
            errorMsg(message.Opcode());
    }

    private void errorMsg(short opcode) {
        Connections.send(connectionId,new ErrorMessage(opcode));
    }

    public void accept(RegisterMessage message){
        if(dataBase.register(message.username, message.password,message.birthday)){
            ackMsg(message.Opcode());
        }
        else{
            errorMsg(message.Opcode());
        }
    }

    private void ackMsg(short opcode) {
        System.out.println("ACK "+opcode);
        Connections.send(connectionId,new AckMessage(opcode));
    }
    private void ackMsg(short opcode,LogStatObj stat){
        AckMessage msg= new AckMessage(opcode);
        msg.setStat(stat);
        Connections.send(connectionId,msg);
    }
    private void ackMsg(short opcode,String username){
        AckMessage msg = new AckMessage(opcode);
        msg.setUsername(username);
        Connections.send(connectionId,msg);
    }

    public void accept(LoginMessage message){
        //System.out.println("accept(Login)");
        if(Connections.getClient(connectionId).hasUser()){
            //System.out.println("Connections.getClient(connectionId).hasUser()");
            errorMsg(message.Opcode());
            return;
        }
        //System.out.println("usename:"+message.username+" password:"+message.password);
        BGSUser user=dataBase.login(message.username, message.password);
        if(user==null){
            //System.out.println("user is null");
            errorMsg(message.Opcode());
        }
        else{
            Connections.getClient(connectionId).setUser(user);
            ackMsg(message.Opcode());
            Queue<BaseMessage> missedMesseges = dataBase.getMissedMessages(message.username);
            for (BaseMessage m : missedMesseges){
                Connections.send(connectionId,m);
            }
        }
    }
    public void accept(FollowMessage message){
        if(Connections.getClient(connectionId).hasUser()){
            if(message.isUnfollow()){
                if(dataBase.unfollow(Connections.getClient(connectionId).username(),message.getToFollow())){
                    ackMsg(message.Opcode(),message.getToFollow());
                }
                else{
                    //System.out.println("MARK I");
                    errorMsg(message.Opcode());
                }

            }
            else{
                if(dataBase.follow(Connections.getClient(connectionId).username(),message.getToFollow())){
                    ackMsg(message.Opcode(),message.getToFollow());
                }
                else{
                    //System.out.println("MARK II");
                    errorMsg(message.Opcode());
                }
            }
        }
        else{
            //System.out.println("MARK III");
            errorMsg(message.Opcode());
        }

    }
    public void accept(BlockMessage message){
        if(!Connections.getClient(connectionId).hasUser()){
            errorMsg(message.Opcode());
            return;
        }
        if(dataBase.block(Connections.getClient(connectionId).username(),message.getUsersToBlock())){
            ackMsg(message.Opcode());
        }
        else{
            errorMsg(message.Opcode());
        }
    }
    public void accept(LogoutMessage message){
        if(!Connections.getClient(connectionId).hasUser())
            errorMsg(message.Opcode());
        else {
            dataBase.logout(Connections.getClient(connectionId).username());
            ackMsg(message.Opcode());
            Connections.getClient(connectionId).setUser(null);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shouldTerminate=true;
            Connections.disconnect(connectionId);
        }
    }
    public void accept(LogStatMessage message){
        if(!Connections.getClient(connectionId).hasUser()){
            errorMsg(message.Opcode());
            return;
        }
        List<LogStatObj> stats = dataBase.logstat(Connections.getClient(connectionId).username());
        if(stats==null){
            errorMsg(message.Opcode());
        }
        else{
            for(LogStatObj o : stats)
               ackMsg(message.Opcode(),o);
        }
    }
    public void accept(PostMessage message){
        if(!Connections.getClient(connectionId).hasUser()){
            errorMsg(message.Opcode());
            return;
        }
        List<BGSUser> postTo = dataBase.getFollowers(Connections.getClient(connectionId).username());
        if(postTo==null){
            errorMsg(message.Opcode());
        }
        else{
            List<BGSUser> tagged=dataBase.getUsers(message.getTaggedUsers());
            for(BGSUser u : tagged){
                postTo.add(u);
            }
            NotificationMessage msg = NotificationMsg(message,Connections.getClient(connectionId).username());
            for(BGSUser u : postTo){
                if(u.isLogged()){
                    dataBase.postMessage(msg,u.getUsername());
                    Connections.send(u.getConnectionId(),msg);
                }
                else {
                    dataBase.addToMissedMesseges(u.getUsername(),msg);
                }
            }
            ackMsg(message.Opcode());
        }

    }

    private NotificationMessage NotificationMsg(PostMessage msg,String from) {
        return new NotificationMessage(msg.toString(),from,false, dataBase.getFiltered());
    }


    public void accept(PrivateMessage message){
        if(!Connections.getClient(connectionId).hasUser()){
            errorMsg(message.Opcode());
        }
        else
        {
            NotificationMessage msg = new NotificationMessage(message.getContent(), Connections.getClient(connectionId).username(), true, dataBase.getFiltered());
            if(dataBase.privateMessage(msg, message.getSendTo())){
                Connections.send(dataBase.getUser(message.getSendTo()).getConnectionId(),msg);
                ackMsg(message.Opcode());
            }
            else{
                if(dataBase.isBlocked(message.getSendTo(),Connections.getClient(connectionId).username())){
                    errorMsg(message.Opcode());
                    return;
                }
                if(!dataBase.addToMissedMesseges(message.getSendTo(), msg))
                    errorMsg(message.Opcode());
                else{
                    ackMsg(message.Opcode());
                }
            }
        }

    }
    public void accept(StatusMessage message){
        if(!Connections.getClient(connectionId).hasUser()){
            errorMsg(message.Opcode());
            return;
        }
        List<LogStatObj> stats = dataBase.status(Connections.getClient(connectionId).username(), message.getUsers());
        if(stats==null){
            errorMsg(message.Opcode());
        }
        else{
            for(LogStatObj o : stats)
              ackMsg(message.Opcode(),o);
        }
    }


    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
