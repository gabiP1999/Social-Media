package bgu.spl.net.impl.newsfeed;

import bgu.spl.net.BGS.BGSConnections;
import bgu.spl.net.BGS.BGSEncoderDecoder;
import bgu.spl.net.BGS.BGSMessagingProtocol;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Server;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NewsFeedServerMain {

    public static void main(String[] args) {







        Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                7777, //port
                BGSMessagingProtocol::new, //protocol factory
                BGSEncoderDecoder::new //message encoder decoder factory
        ).serve();


    }
}
