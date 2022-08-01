package bgu.spl.net.BGS;

public class Buffer {
    private byte[] bytes;
    private int size;
    public Buffer(int capacity){
        this.size=0;
        bytes=new byte[capacity];
    }
    public void add(byte b){
        if (size>=bytes.length-1){
            byte[] arr = new byte[bytes.length*2];
            for(int i=0;i< bytes.length;i++){
                arr[i] = bytes[i];
            }
            bytes=arr;
        }
        else{
            bytes[size]=b;
        }
        size++;
    }
    public void reset(){size=0;}

    public byte[] getBytes() {
        byte[] arr=new byte[size];
        for(int i=0;i<size;i++)
            arr[i]=bytes[i];
        return arr;
    }

    public int getSize() {
        return size;
    }
}
