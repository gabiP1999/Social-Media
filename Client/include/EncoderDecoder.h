//
// Created by gabi4 on 04/01/2022.
//
#include <vector>
#include <memory>
#include "BaseMessage.h"

#ifndef UNTITLED2_ENCODERDECODER_H
#define UNTITLED2_ENCODERDECODER_H
using byte =  char;

class EncoderDecoder {
public:
    std::unique_ptr<std::string> encode(const BaseMessage & message);
    std::unique_ptr<BaseMessage> decodeNextByte(byte nextByte);
    EncoderDecoder();

private:
    std::vector<byte> byteVector;
    std::unique_ptr<BaseMessage> buildMessage();
    static void encodeMessage(const BaseMessage &message, std::string &data) ;
    static void shortToBytes(short num, char* bytesArr) ;
    static short bytesToShort(char* bytesArr);
    static void handleRegister(const BaseMessage &message, std::string &basicString) ;
    static void handleLogin(const BaseMessage &message, std::string &basicString) ;
    static void handleLogout(const BaseMessage &message, std::string &basicString) ;
    static void handleFollow(const BaseMessage &message, std::string &basicString) ;
    static void handlePost(const BaseMessage &message, std::string &basicString) ;
    static void handlePM(const BaseMessage &message, std::string &basicString) ;
    static void handleLogstat(const BaseMessage &message, std::string &basicString) ;
    static void handleStatus(const BaseMessage &message, std::string &basicString) ;
    static void handleBlock(const BaseMessage &message, std::string &basicString) ;
    std::unique_ptr<NotificationMessage> buildNotification();
    std::unique_ptr<AckMessage> buildAck();
    std::unique_ptr<ErrorMessage> buildError();
    std::string generateString(unsigned int i);
};


#endif //UNTITLED2_ENCODERDECODER_H
