package bgu.spl.net.BGS;

import java.nio.charset.Charset;

public class StringGenerator {
    private Buffer buffer;
    private Charset encode;
    public StringGenerator(Buffer buffer,Charset encode){
        this.buffer=buffer;this.encode=encode;
    }
    public String generate(){
        String res = new String(buffer.getBytes(),0,buffer.getSize(),encode);
        buffer.reset();
        return res;
    }
}
