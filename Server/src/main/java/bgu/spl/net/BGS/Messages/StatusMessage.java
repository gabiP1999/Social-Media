package bgu.spl.net.BGS.Messages;

import bgu.spl.net.BGS.BGSMessagingProtocol;

import java.util.List;

public class StatusMessage extends BaseMessage {
    private List<String> users ;
    public StatusMessage(List<String> users){
        this.users=users;
        setOpcode((short) 8);
        setValid(true);
    }
    public void visit(BGSMessagingProtocol bgsMessagingProtocol) {
        bgsMessagingProtocol.accept(this);
    }

    @Override
    public String toString() {
        return "StatusMessage"+ super.toString();
    }

    public List<String> getUsers() {
        return users;
    }
}
