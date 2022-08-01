Dear User, this is for you :) have fun!

HOW TO RUN YOUR CODE:
1.open the Terminal in the Server folder.
2.write the command: 
	*for Reactor: 
	mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain" -Dexec.args="<port> <number of threads>"
	*for Thred Per Client:
	mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain" -Dexec.args="<port>"
3.open the Terminal in the Client folder.
4.write the command: make
5.write the command: bin/Client <port>

ALL THE ACTION MUST BE WRITTEN IN ALL CAPITAL LETTER:
Action examples:
REGISTER shir 123 28-12-1997
LOGIN shir 123 1
LOGOUT
FOLLOW 0 gabi
FOLLOW 1 gabi //unfollow
POST some mesage for @marina
PM shir hii
LOGSTAT
STAT shir gabi marina
BLOCK gabi

THE CODE THAT STORED THE FILTERED SET OF WORD IS IN :
Server.src.main.java.bgu.spl.net.BGS.BGSDataBase.java - in array that called filtered.
