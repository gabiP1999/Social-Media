package bgu.spl.net.api;

import bgu.spl.net.srv.ConnectionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T>  {
    protected Map<Integer, ConnectionHandler<T>> connectionHandlers;
    protected int connectionId;
    public ConnectionsImpl(){
        connectionHandlers=new ConcurrentHashMap<>();
        connectionId=0;
    }
    public int connect(ConnectionHandler<T> handler){
        connectionHandlers.put(connectionId,handler);
        int ret = connectionId;
        connectionId++;
        return ret;
    }
    @Override
    public boolean send(int connectionId, T msg) {
       if(!connectionHandlers.containsKey(connectionId))return false;
       connectionHandlers.get(connectionId).send(msg);
       return true;
    }

    @Override
    public void broadcast(T msg) {
        for(ConnectionHandler<T> handler : connectionHandlers.values())
            handler.send(msg);
    }

    @Override
    public void disconnect(int connectionId) {
        if(!connectionHandlers.containsKey(connectionId))return;
        try {
            connectionHandlers.get(connectionId).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionHandlers.remove(connectionId);
    }
}
