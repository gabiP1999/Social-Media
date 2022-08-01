package bgu.spl.net.BGS.Messages;


import bgu.spl.net.BGS.BGSMessagingProtocol;

import java.util.List;

public class PrivateMessage extends BaseMessage {
    private String sendTo;
    private String content;

    public PrivateMessage(List<String> content, String sendTo) {
        this.content="";
        for(String s : content){
            this.content+=" "+s;
        }
        if(this.content.length()>0)this.content=this.content.substring(1);
        this.sendTo = sendTo;
        setValid(true);
        setOpcode((short) 6);
    }
    public String getSendTo(){return sendTo;}

    public void visit(BGSMessagingProtocol bgsMessagingProtocol) {
        bgsMessagingProtocol.accept(this);
    }
    @Override
    public String toString() {
        return "PrivateMessage{" +
                "sendTo='" + sendTo + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }
}

