package bgu.spl.net.impl.newsfeed;

import bgu.spl.net.BGS.BGSEncoderDecoder;
import bgu.spl.net.BGS.Buffer;
import bgu.spl.net.BGS.Messages.*;
import bgu.spl.net.BGS.StringGenerator;
import bgu.spl.net.impl.rci.RCIClient;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NewsFeedClientMain {

    public static void main(String[] args) throws Exception {
        //if (args.length == 0) {
          //  args = new String[]{"127.0.0.1"};
        //}

//        System.out.println("running clients");
        //runFirstClient(args[0]);
       // runSecondClient(args[0]);
       // runThirdClient(args[0]);
//        String date = "12-09-1999";
//        for(byte b : date.getBytes(StandardCharsets.UTF_8)){
//            System.out.print(b+",");
//        }

        byte[] arr = {0,1,71,97,98,105,0,49,50,51,0,49,50,45,48,57,45,49,57,57,57,59};
        BGSEncoderDecoder encoderDecoder = new BGSEncoderDecoder();
        int index = 0;
        BaseMessage message = null;
        while(index<arr.length){
            message= encoderDecoder.decodeNextByte(arr[index]);
            index++;
        }
        System.out.println(message);
//        Buffer b = new Buffer(1<<16);
//        b.add((byte) 71);b.add((byte) 97);b.add((byte) 98);b.add((byte) 105);
//        StringGenerator stringGenerator = new StringGenerator(b,StandardCharsets.UTF_8);
//        System.out.println(stringGenerator.generate());
//        b.add((byte) 71);b.add((byte) 97);b.add((byte) 98);b.add((byte) 105);
//        System.out.println(stringGenerator.generate());
    }

    private static void runFirstClient(String host) throws Exception {
        try (RCIClient c = new RCIClient(host, 7777)) {
            c.send(new PublishNewsCommand(
                    "jobs",
                    "System Programmer, knowledge in C++, Java and Python required. call 0x134693F"));

            c.receive(); //ok

            c.send(new PublishNewsCommand(
                    "headlines",
                    "new SPL assignment is out soon!!"));

            c.receive(); //ok

            c.send(new PublishNewsCommand(
                    "headlines",
                    "THE CAKE IS A LIE!"));

            c.receive(); //ok
        }

    }

    private static void runSecondClient(String host) throws Exception {
        try (RCIClient c = new RCIClient(host, 7777)) {
            c.send(new FetchNewsCommand("jobs"));
            System.out.println("second client received: " + c.receive());
        }
    }

    private static void runThirdClient(String host) throws Exception {
        try (RCIClient c = new RCIClient(host, 7777)) {
            c.send(new FetchNewsCommand("headlines"));
            System.out.println("third client received: " + c.receive());
        }
    }
}
