package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.BGS.BGSEncoderDecoder;
import bgu.spl.net.BGS.BGSMessagingProtocol;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
        Server.threadPerClient(
                7777, //port
                BGSMessagingProtocol::new, //protocol factory
                BGSEncoderDecoder::new//message encoder decoder factory
        ).serve();
    }
}
