package bgu.spl.net.BGS;

import bgu.spl.net.BGS.Messages.BaseMessage;
import bgu.spl.net.srv.ConnectionHandler;


public class BGSClient {

    private BGSUser user;
    private ConnectionHandler<BaseMessage> handler;
    private int connectionId;
    public BGSClient(ConnectionHandler<BaseMessage> handler,int connectionId){
        this.handler = handler;
        user=null;
        this.connectionId = connectionId;
    }
    public boolean hasUser(){
        return user!=null;
    }
    public void setUser(BGSUser u){
        user=u;
        if(user!=null)user.setConnectionId(connectionId);
    }
    public ConnectionHandler<BaseMessage> getHandler(){return handler;}
    public String username(){return user.getUsername();}
}
