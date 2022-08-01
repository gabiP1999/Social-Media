package bgu.spl.net.BGS.Messages;

import bgu.spl.net.BGS.BGSMessagingProtocol;

import java.util.List;

public class PostMessage extends BaseMessage {
    private final List<String> taggedUsers;
    private String content;
    public PostMessage(List<String> content,List<String> tagged_users) {
        this.content="";
        for(String s : content){
            this.content+=" "+s;
        }
        if(this.content.length()>0)this.content=this.content.substring(1);
        setOpcode((short) 5);
        setValid(true);
        taggedUsers=tagged_users;
    }
    public void visit(BGSMessagingProtocol bgsMessagingProtocol) {
        bgsMessagingProtocol.accept(this);
    }
    public List<String> getTaggedUsers(){return taggedUsers;}

    @Override
    public String toString() {
        return content;
    }
}
