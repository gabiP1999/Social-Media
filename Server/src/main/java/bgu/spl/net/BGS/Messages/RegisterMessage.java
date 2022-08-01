package bgu.spl.net.BGS.Messages;

import bgu.spl.net.BGS.BGSMessagingProtocol;

import java.util.Date;

public class RegisterMessage extends BaseMessage{
    public BGSDate birthday;
    public String username;
    public String password;
    public RegisterMessage(String username, String password, BGSDate birth){
        //System.out.println("creaing new Register message");
        this.birthday = birth;
        this.username=username;
        this.password=password;
        setValid(true);
        setOpcode((short) 1);
    }
    public void visit(BGSMessagingProtocol bgsMessagingProtocol) {
        bgsMessagingProtocol.accept(this);
    }

    @Override
    public String toString() {
        return "RegisterMessage{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", Birthday='" + birthday + '\'' +
                '}';
    }
}
