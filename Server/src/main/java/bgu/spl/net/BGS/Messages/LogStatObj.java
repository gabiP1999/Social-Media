package bgu.spl.net.BGS.Messages;

public class LogStatObj {

    public short age;
    public short numPosts;
    public short NumFollowers;
    public short numFollowing;

    public LogStatObj(short age,short  numPosts ,short NumFollowers,short numFollowing)
    {
        this.age= age;
        this.NumFollowers = NumFollowers;
        this.numFollowing= numFollowing;
        this.numPosts = numPosts;
    }

}
