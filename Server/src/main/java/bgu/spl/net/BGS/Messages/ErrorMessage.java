package bgu.spl.net.BGS.Messages;

import bgu.spl.net.BGS.BGSEncoderDecoder;

public class ErrorMessage extends BaseMessage {
    private short Msgopcode;
    public ErrorMessage(short Msgopcode){
        this.Msgopcode=Msgopcode;
        setOpcode((short) 11);
    }

    @Override
    public String toString() {
        return "ERROR ";
    }
    public byte[] visit(BGSEncoderDecoder enc){
        return enc.accept(this);
    }
    public short getMsgopcode(){return Msgopcode;}
}
