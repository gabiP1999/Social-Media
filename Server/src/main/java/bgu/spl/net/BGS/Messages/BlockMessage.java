package bgu.spl.net.BGS.Messages;

import bgu.spl.net.BGS.BGSMessagingProtocol;

import java.util.List;

public class BlockMessage extends BaseMessage {
    private List<String> UsersToBlock;
    public BlockMessage(List<String> toBlock){
        setOpcode((short) 12);
        setValid(true);
        UsersToBlock = toBlock;
    }

    public List<String> getUsersToBlock() {
        return UsersToBlock;
    }

    @Override
    public void visit(BGSMessagingProtocol bgsMessagingProtocol) {
        bgsMessagingProtocol.accept(this);
    }

    @Override
    public String toString() {
        return "BlockMessage{" +
                "UsersToBlock=" + UsersToBlock +
                '}';
    }
}
