package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.BGS.BGSEncoderDecoder;
import bgu.spl.net.BGS.BGSMessagingProtocol;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {
        if(args.length<2){
            System.out.println("Missing port number.");
            return;
        }
        short port = Short.parseShort(args[0]);
        int numThread = Integer.parseInt(args[1]);
        //short port = 7777;
        Server.reactor(
                numThread,
                port, //port
                BGSMessagingProtocol::new, //protocol factory
                BGSEncoderDecoder::new //message encoder decoder factory
        ).serve();
    }
}
