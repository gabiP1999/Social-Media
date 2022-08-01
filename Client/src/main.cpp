#include <iostream>
#include <thread>
#include "../include/ConnectionHandler.h"
#include "../include/EncoderDecoder.h"
#include "../include/MessagePrinter.h"
#include "../include/BGSConnectionHandler.h"
#include <boost/algorithm/string.hpp>
#include <vector>


std::vector<std::string> Split(std::string toSplit){
    std::vector<std::string> s;
    std::vector<std::string> res;
    boost::split(s,toSplit,boost::is_any_of(" "));
    for(std::string a :s){
        if (a.length()>0 && a[0]!=' ')res.push_back(a);
    }
    return res;
}

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/



void Read(BGSConnectionHandler& conn, bool& shouldTerminate){
    while (!shouldTerminate){
    std::cout<<"Reading.."<<std::endl;
    BaseMessage *message = nullptr;
    std::unique_ptr<BaseMessage> baseMessage(message);
        if (!conn.readMessage(baseMessage)) {
            shouldTerminate=true;
        }
        delete message;
    }
}

int main (int argc, char *argv[]) {
    if (argc < 2) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl;
        return -1;
    }
    //std::string host = argv[1];
    short port = atoi(argv[1]);

    std::string host ="127.0.0.1";
    //short port = 7777;
    EncoderDecoder encoderDecoder;
    BGSConnectionHandler conn(host, port,encoderDecoder);
    bool shouldTerminate=false;

    if (!conn.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    std::thread printerTh(Read,std::ref(conn),std::ref(shouldTerminate));



    while(!shouldTerminate){
        std::string cmd;
        std::getline(std::cin, cmd);
        if(!cmd.empty()){
            //std::cout<<"cmd is "<<cmd<<std::endl;
            std::vector<std::string> args;
            std::vector<std::string> arguments = Split(cmd);
            std::string type = arguments[0];
            for(unsigned int i=1;i<arguments.size();i++){
                args.push_back(arguments[i]);
            }
            //std::cout<<"args.size()="<<args.size()<<std::endl;
            if(type=="REGISTER"){
                if(args.size()<3)std::cout<<"Missing Params"<<std::endl;
                else {
                    RegisterMessage message ;
                    message.setContent(args);
                    if(!conn.writeMessage(message)){
                        shouldTerminate=true;
                    }
                }

            }
            if(type=="LOGIN"){
                if(args.size()<3)std::cout<<"Missing Params"<<std::endl;
                else {
                    LoginMessage message;
                    message.setContent(args);
                    if(!conn.writeMessage(message)){
                        shouldTerminate=true;
                    }
                }
            }
            if(type=="LOGOUT"){
                LogoutMessage message;
                if(!conn.writeMessage(message)){
                    shouldTerminate=true;
                }
            }
            if(type=="FOLLOW"){
                if(args.size()<2)std::cout<<"Missing Params"<<std::endl;
                else{
                    FollowMessage message;
                    message.setContent(args);
                    if(!conn.writeMessage(message)){
                        shouldTerminate=true;
                    }
                }
            }
            if(type=="POST"){
                if(args.empty())std::cout<<"Missing Params"<<std::endl;
                else{
                    PublicMessage message;
                    message.setContent(args);
                    if(!conn.writeMessage(message)){
                        shouldTerminate=true;
                    }
                }
            }
            if(type=="PM"){
                if(args.size()<2)std::cout<<"Missing Params"<<std::endl;
                else{
                    PrivateMessage message;
                    message.setContent(args);
                    if(!conn.writeMessage(message)){
                        shouldTerminate=true;
                    }
                }

            }
            if(type=="LOGSTAT"){
                LogstatMessage message;
                if(!conn.writeMessage(message)){
                    shouldTerminate=true;
                }
            }
            if(type=="STAT"){
                if(args.empty())std::cout<<"Missing Params"<<std::endl;
                else{
                    StatusMessage message;
                    message.setContent(args);
                    if(!conn.writeMessage(message)){
                        shouldTerminate=true;
                    }
                }
            }
            if(type=="BLOCK"){
                if(args.size()<1)std::cout<<"Missing Params"<<std::endl;
                else{
                    BlockMessage message;
                    message.setContent(args);
                    if(!conn.writeMessage(message)){
                        shouldTerminate=true;
                    }
                }
            }
        }
    }
    printerTh.join();
    std::cout<<"Program Finished.."<<std::endl;

	//From here we will see the rest of the ehco client implementation:

		// connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
        // We can use one of three options to read data from the server:
        // 1. Read a fixed number of characters
        // 2. Read a line (up to the newline character using the getline() buffered reader
        // 3. Read up to the null character
        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end

		// A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
		// we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
    return 0;
    }




