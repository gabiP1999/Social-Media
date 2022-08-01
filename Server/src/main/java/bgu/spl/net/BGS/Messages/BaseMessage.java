package bgu.spl.net.BGS.Messages;

import bgu.spl.net.BGS.BGSEncoderDecoder;
import bgu.spl.net.BGS.BGSMessagingProtocol;
import bgu.spl.net.api.Message;

public class BaseMessage implements Message {
    private boolean isValid;
    private short opcode;


    public boolean isValid() {
        return isValid;
    }
    public void setValid(boolean isValid){
        this.isValid = isValid;
    }
    protected void setOpcode(short op){
        opcode=op;
    }
    public short Opcode(){return opcode;}

    @Override
    public String toString() {
        return "BaseMessage{" +
                "isValid=" + isValid +
                ", opcode='" + opcode + '\'' +
                '}';
    }

    public void visit(BGSMessagingProtocol bgsMessagingProtocol) {

    }
    public byte[] visit(BGSEncoderDecoder enc){
        return new byte[0];
    }
}


