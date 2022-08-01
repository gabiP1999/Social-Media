package bgu.spl.net.BGS.Messages;

import bgu.spl.net.BGS.BGSMessagingProtocol;

public class LogoutMessage extends BaseMessage {
    public LogoutMessage(){
        setOpcode((short) 3);
        setValid(true);
    }
    public void visit(BGSMessagingProtocol bgsMessagingProtocol) {
        bgsMessagingProtocol.accept(this);
    }
    @Override
    public String toString() {
        return "LogoutMessage "+super.toString();
    }
}
