package bgu.spl.net.BGS.Messages;

import bgu.spl.net.BGS.BGSMessagingProtocol;

public class LogStatMessage extends BaseMessage{
    public LogStatMessage(){
        setOpcode((short) 7);
        setValid(true);
    }
    public void visit(BGSMessagingProtocol bgsMessagingProtocol) {
        bgsMessagingProtocol.accept(this);
    }

    @Override
    public String toString() {
        return "LogStat "+super.toString();
    }
}
