package bgu.spl.net.BGS.Messages;

import bgu.spl.net.BGS.BGSMessagingProtocol;

import java.util.List;

public class FollowMessage extends BaseMessage{
    private boolean unfollow;
    private String toFollow;
    public FollowMessage(String toFollow) {
        unfollow = false;
        this.toFollow=toFollow;
        setOpcode((short) 4);
        setValid(true);
    }
    public String getToFollow(){return toFollow;}
    public void setUnfollow(){unfollow=true;}

    public boolean isUnfollow() {
        return unfollow;
    }
    public void visit(BGSMessagingProtocol bgsMessagingProtocol) {
        bgsMessagingProtocol.accept(this);
    }

    @Override
    public String toString() {
        return "FollowMessage{" +
                "unfollow=" + unfollow +
                ", toFollow=" + toFollow +
                '}';
    }
}
