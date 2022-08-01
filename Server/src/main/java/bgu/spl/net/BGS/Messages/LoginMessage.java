package bgu.spl.net.BGS.Messages;

import bgu.spl.net.BGS.BGSMessagingProtocol;

public class LoginMessage extends BaseMessage{
    public String username;
    public String password;
    public byte chapta;
    public LoginMessage(String user,String pass,byte chapta){
        this.chapta=chapta;
        username=user;
        password=pass;
        setOpcode((short) 2);
        setValid(true);
    }
    public void visit(BGSMessagingProtocol bgsMessagingProtocol) {
        bgsMessagingProtocol.accept(this);
    }

    @Override
    public String toString() {
        return "LoginMessage{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
