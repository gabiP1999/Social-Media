package bgu.spl.net.BGS.Messages;

import bgu.spl.net.BGS.BGSEncoderDecoder;

public class NotificationMessage extends BaseMessage{
    private byte type;
    private String sender;
    private String content;
    public NotificationMessage(String content,String sender,boolean isPrivate,String[] filtered){
       for(int i =0;i< filtered.length;i++){
           int index = content.indexOf(filtered[i]);
           if(index!= -1){
               String before = content.substring(0,index);
               String after = content.substring(index+filtered[i].length());
               String filter = "<filtered>";
               content=before+filter+after;
           }
       }
       this.content=content;
        this.sender=sender;
        if(!isPrivate)type=1;
        else type=0;
        setOpcode((short) 9);
    }

    @Override
    public String toString() {
        return "NOTIFICATION "+type+" "+sender+""+content;
    }
    public String getSender(){return sender;}

    public byte[] visit(BGSEncoderDecoder enc){
        return enc.accept(this);
    }

    public byte getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
