package bgu.spl.net.BGS;

import bgu.spl.net.BGS.Messages.BaseMessage;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.api.ConnectionsImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BGSConnections extends ConnectionsImpl<BaseMessage> {
    private Map<Integer,BGSClient> Clients;

    public BGSConnections(){
        super();
        Clients = new ConcurrentHashMap<>();
    }
    @Override
    public int connect(ConnectionHandler<BaseMessage> handler) {
        Clients.put(connectionId,new BGSClient(handler,connectionId));
        return super.connect(handler);
    }
    @Override
    public void disconnect(int connectionId) {
        Clients.remove(connectionId);
        super.disconnect(connectionId);
    }
    public BGSClient getClient(int connectionId){
        return Clients.get(connectionId);
    }

}
