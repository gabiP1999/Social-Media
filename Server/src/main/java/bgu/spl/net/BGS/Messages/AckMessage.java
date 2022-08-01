package bgu.spl.net.BGS.Messages;

import bgu.spl.net.BGS.BGSEncoderDecoder;

import java.util.ArrayList;
import java.util.List;

public class AckMessage extends BaseMessage{
    private LogStatObj stats;
    private boolean hasStats;
    private boolean hasUsername;
    private String username;
    private short messageOpcode;
    public AckMessage(short messageOpcode){
        hasStats=false;
        username="";
        hasUsername=false;
        this.messageOpcode = messageOpcode;
        setOpcode((short) 10);
    }
    public void setStat(LogStatObj o){
        this.stats=o;
        hasStats=true;
    }
    public boolean isHasStats(){return hasStats;}
    public void setUsername(String username){
        this.username=username;
        hasUsername=true;
    }
    public boolean isHasUsername(){return hasUsername;}

    public byte[] visit(BGSEncoderDecoder enc){
        return enc.accept(this);
    }

    public short getMessageOpcode() {
        return messageOpcode;
    }

    public LogStatObj getStat() {
        return stats;
    }

    public String getUsername() {
        return username;
    }
}
