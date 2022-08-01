package bgu.spl.net.BGS;

import bgu.spl.net.BGS.Messages.AckMessage;
import bgu.spl.net.BGS.Messages.BaseMessage;
import bgu.spl.net.BGS.Messages.ErrorMessage;
import bgu.spl.net.BGS.Messages.NotificationMessage;
import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageEncoderDecoder;

import java.io.FilterOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BGSEncoderDecoder<MessageBuilder> implements MessageEncoderDecoder<BaseMessage> {
    private final char ZERO = '\0';
    private final char SPACE = ' ';
    private final char CHTRUDLE = '@';
    private final char END =';';
    private Buffer buffer;
    private StringGenerator generator;
    private Charset encode;
    private short OPCODE;
    private List<String> tagged;
    private List<String> content;
    private byte FOLLOW;



    public BGSEncoderDecoder(){
        this.encode = Charset.forName(StandardCharsets.UTF_8.name());
        buffer=new Buffer(1<<13);
        generator= new StringGenerator(buffer,encode);
        OPCODE=-1;
        FOLLOW=-1;
        tagged = new ArrayList<>();
        content = new ArrayList<>();
    }



    @Override
    public BaseMessage decodeNextByte(byte nextByte) {
        if(nextByte==END){
            String s = generator.generate();
            if(s.length()!=0){
                if(s.charAt(0)==CHTRUDLE)tagged.add(s.substring(1));
                content.add(s);
            }
            BaseMessage ret = BuildMessage();
            reset();
            return ret;
        }
        if(nextByte!=ZERO|OPCODE==-1)decode(nextByte);
        if(OPCODE==4 & FOLLOW==-1){
            FOLLOW=nextByte;
            return null;
        }
        if((OPCODE==-1)&buffer.getSize()==2){
            generateOpcode();
            return null;
        }

        if(nextByte==ZERO & OPCODE!=-1 ){
            //System.out.println("IF");
            String s = generator.generate();
            //System.out.println(s);
            if(s.length()!=0){
                if(s.charAt(0)==CHTRUDLE)tagged.add(s.substring(1));
                content.add(s);
            }
        }
        return null;
    }

    private void reset() {
        OPCODE=-1;
        FOLLOW=-1;
        content=new ArrayList<>();
        tagged=new ArrayList<>();
    }


    private void generateOpcode() {
        //tem.out.println("generate");
        //for(byte b: buffer.getBytes())// System.out.print(b+",");
        OPCODE=bytesToShort(buffer.getBytes());
        //System.out.println(OPCODE);
        buffer.reset();
    }

    private BaseMessage BuildMessage(){
        //System.out.println("opcode ="+OPCODE+"\ncontent:");
        //for(String s :content) System.out.print(s+",");
        //System.out.println("follow="+FOLLOW);
        return bgu.spl.net.BGS.MessageBuilder.Build(OPCODE,content,tagged,FOLLOW);
    }

    @Override
    public byte[] encode(BaseMessage message) {
        return message.visit(this);
    }
    private void decode(byte b){
        buffer.add(b);
    }
    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    public byte[] accept(AckMessage Message) {
        byte[] AckOp = shortToBytes(Message.Opcode());
        byte[] msgOp = shortToBytes(Message.getMessageOpcode());
        if(Message.isHasStats()){
            byte[] age = shortToBytes(Message.getStat().age);
            byte[] numPosts = shortToBytes(Message.getStat().numPosts);
            byte[] numFollowers = shortToBytes(Message.getStat().NumFollowers);
            byte[] numFollowing = shortToBytes(Message.getStat().numFollowing);
            byte[] ans = new byte[13];
            ans[0]=AckOp[0];ans[1]=AckOp[1];
            ans[2]=msgOp[0];ans[3]=msgOp[1];
            ans[4]=age[0];ans[5]=age[1];
            ans[6]=numPosts[0];ans[7]=numPosts[1];
            ans[8]=numFollowers[0];ans[9]=numFollowers[1];
            ans[10]=numFollowing[0];ans[11]=numFollowing[1];
            ans[12]=END;
            return ans;
        }
        else if(Message.isHasUsername()){
            String username = Message.getUsername();
            byte[] strArr = username.getBytes(StandardCharsets.UTF_8);
            byte[] ans = new byte[6+strArr.length];
            ans[0]=AckOp[0];ans[1]=AckOp[1];
            ans[2]=msgOp[0];ans[3]=msgOp[1];
            System.arraycopy(strArr, 0, ans, 4, strArr.length);
            ans[ans.length-2]='\0';
            ans[ans.length-1]=';';
            return ans;

        }
        else{
            byte[] ans = new byte[5];
            ans[0]=AckOp[0];ans[1]=AckOp[1];
            ans[2]=msgOp[0];ans[3]=msgOp[1];
            ans[4]=END;
            return ans;
        }
    }
    public byte[] accept(ErrorMessage Message) {
        byte[] errorOp = shortToBytes(Message.Opcode());
        byte[] msgOp = shortToBytes(Message.getMsgopcode());
        byte[] ans = new byte[5];
        ans[0]=errorOp[0];ans[1]=errorOp[1];
        ans[2]=msgOp[0];ans[3]=msgOp[1];
        ans[4]=END;
        return ans;
    }
    public byte[] accept(NotificationMessage Message) {
        int index =3;
        byte[] posting_user=Message.getSender().getBytes(StandardCharsets.UTF_8);
        byte[] message_content = Message.getContent().getBytes(StandardCharsets.UTF_8);
        byte type = Message.getType();
        byte[] op = shortToBytes(Message.Opcode());
        byte zero = 0;
        byte[] ans = new byte[posting_user.length + message_content.length +6];
        ans[0]=op[0];ans[1]=op[1];ans[2]=type;
        System.arraycopy(posting_user, 0, ans, index + 0, posting_user.length);
        index=index+posting_user.length;
        ans[index]=zero;
        index++;
        System.arraycopy(message_content, 0, ans, index + 0, message_content.length);
        ans[ans.length-2] = zero;
        ans[ans.length-1] = END;
        return ans;
    }
}
