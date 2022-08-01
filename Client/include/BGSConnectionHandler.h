//
// Created by gabi4 on 07/01/2022.
//

#ifndef UNTITLED2_BGSCONNECTIONHANDLER_H
#define UNTITLED2_BGSCONNECTIONHANDLER_H


#include "ConnectionHandler.h"
#include "BaseMessage.h"
#include "EncoderDecoder.h"

class BGSConnectionHandler: public ConnectionHandler {
private:
    EncoderDecoder& encoderDecoder;
public:
    BGSConnectionHandler(std::string host,short port,EncoderDecoder& encoderDecoder);
    bool readMessage(std::unique_ptr<BaseMessage> &message);
    bool writeMessage(const BaseMessage &message);
};


#endif //UNTITLED2_BGSCONNECTIONHANDLER_H
