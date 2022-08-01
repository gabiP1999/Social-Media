//
// Created by gabi4 on 07/01/2022.
//

#include "../include/BGSConnectionHandler.h"

bool BGSConnectionHandler::readMessage(std::unique_ptr<BaseMessage> &message) {
    bool ans;
    byte  curr = 0;
    while ((ans = getBytes(&curr, 1))) {
        message = encoderDecoder.decodeNextByte(curr);
        if (message){
            std::cout<<message->toString()<<std::endl;
        }
    }
    return ans;
}

bool BGSConnectionHandler::writeMessage(const BaseMessage &message) {
    std::unique_ptr<std::string> string_message(encoderDecoder.encode(message));
    //for(int i=0;i<string_message->length();i++){
       // std::cout<<(int)string_message->c_str()[i]<<",";
    //}
    //std::cout<<std::endl;
    return sendBytes(string_message->c_str(), string_message->length());
}

BGSConnectionHandler::BGSConnectionHandler(std::string host, short port, EncoderDecoder &encoderDecoder): ConnectionHandler(host,port),encoderDecoder(encoderDecoder) {
}

