//
// Created by gabi4 on 04/01/2022.
//

#include <iostream>
#include "../include/EncoderDecoder.h"

std::unique_ptr<std::string> EncoderDecoder::encode(const BaseMessage &message) {
    auto *data = new std::string();
    encodeMessage(message, *data);
    return std::unique_ptr<std::string>(data);
}

std::unique_ptr<BaseMessage> EncoderDecoder::decodeNextByte(byte nextByte) {
    if(nextByte != ';') {
        byteVector.push_back(nextByte);
    }
    else {
        return buildMessage();
    }
    return nullptr;
}

EncoderDecoder::EncoderDecoder() : byteVector() { }

std::unique_ptr<BaseMessage> EncoderDecoder::buildMessage() {
    byte bytes[] = {byteVector[0],byteVector[1]};
    short opcode = bytesToShort(bytes);
    std::unique_ptr<BaseMessage> ret;
    if(opcode==(short)9){ret= buildNotification();}
    else if(opcode==(short)10){ret= buildAck();}
    else if(opcode==(short)11){ret= buildError();}
    byteVector.clear();
    return ret;
}





void EncoderDecoder::encodeMessage(const BaseMessage &message, std::string &data) {
    char opcode[2] ={};
    shortToBytes(message.getType(),opcode);
    //std::cout<<"opcode is "<<message.getType()<<std::endl;
    data+=opcode[0];
    data+=opcode[1];
    //std::cout<<"code =  "<<code<<" " <<(int)code<<std::endl;
    //std::cout<<"data is "<<data<<std::endl;
    //std::cout<<"opcode[0]="<<opcode[0]<<"opcode[1]="<<opcode[1]<<std::endl;
    if(message.getType()==(short)1){handleRegister(message,data);}
    else if(message.getType()==(short)2){handleLogin(message,data);}
    else if(message.getType()==(short)3){handleLogout(message,data);}
    else if(message.getType()==(short)4){handleFollow(message,data);}
    else if(message.getType()==(short)5){handlePost(message,data);}
    else if(message.getType()==(short)6){handlePM(message,data);}
    else if(message.getType()==(short)7){handleLogstat(message,data);}
    else if(message.getType()==(short)8){handleStatus(message,data);}
    else if(message.getType()==(short)12){handleBlock(message,data);}



}









void EncoderDecoder::shortToBytes(short num, char *bytesArr) {
        bytesArr[0] = ((num >> 8) & 0xFF);
        bytesArr[1] = (num & 0xFF);
    }

short EncoderDecoder::bytesToShort(char *bytesArr)
{
    auto result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void EncoderDecoder::handleRegister(const BaseMessage &message, std::string &basicString) {
for(std::string s : message.getContent()){
    basicString.append(s);
    basicString+='\0';
}
basicString+=';';
}

void EncoderDecoder::handleLogin(const BaseMessage &message, std::string &basicString) {
std::vector<std::string> args = message.getContent();
basicString.append(args[0]);basicString+='\0';
basicString.append(args[1]);basicString+='\0';
if(args[2]=="1"){
    basicString+='\1';
}
else basicString+='\0';
basicString+=';';
}

void EncoderDecoder::handleLogout(const BaseMessage &message, std::string &basicString) {
basicString+=';';
}

void EncoderDecoder::handleFollow(const BaseMessage &message, std::string &basicString) {
    std::vector<std::string> args = message.getContent();
    if(args[0]=="1")basicString+='\1';
    else basicString+='\0';
    basicString.append(args[1]);basicString+='\0';
    basicString+=';';

}

void EncoderDecoder::handlePost(const BaseMessage &message, std::string &basicString) {
    std::vector<std::string> args = message.getContent();
    for(std::string s:args){
        basicString.append(s);
        basicString+=" ";
    }
    basicString+='\0';basicString+=';';
}

void EncoderDecoder::handlePM(const BaseMessage &message, std::string &basicString) {
    std::vector<std::string> args = message.getContent();
    for(std::string s :args){
        basicString.append(s);
        basicString+='\0';
    }
    basicString+=';';
}

void EncoderDecoder::handleLogstat(const BaseMessage &message, std::string &basicString) {
    basicString+=';';
}

void EncoderDecoder::handleStatus(const BaseMessage &message, std::string &basicString) {
    std::vector<std::string> args = message.getContent();
    for(unsigned int i =0; i<args.size()-1;i++){
        basicString.append(args[i]);
        basicString+='|';
    }
    basicString.append(args[args.size()-1]);
    basicString+='\0';
    basicString+=';';
}

void EncoderDecoder::handleBlock(const BaseMessage &message, std::string &basicString) {
    std::vector<std::string> args = message.getContent();
    basicString.append(args[0]);
    basicString+='\0';
    basicString+=';';
}

std::unique_ptr<NotificationMessage> EncoderDecoder::buildNotification() {
    auto * message = new NotificationMessage();
    if(byteVector[2]=='\0')message->setPrivate(true);
    else message->setPrivate(false);
    std::string postingUser = generateString(3);
    std::string content = generateString(4+postingUser.size());
    std::vector<std::string> message_content = {postingUser,content};
    message->setContent(message_content);
    return std::unique_ptr<NotificationMessage>(message);
}

std::unique_ptr<AckMessage> EncoderDecoder::buildAck() {
    auto * message = new AckMessage();
    byte bytes[] = {byteVector[2],byteVector[3]};
    short opcode = bytesToShort(bytes);
    message->setOp(opcode);
    if(byteVector.size()==4){
        message->setHasUsername(false);
        message->setHasStatus(false);
        return std::unique_ptr<AckMessage>(message);
    }
    else if(opcode == 4){
        std::string username = generateString(4);
        message->setUsername(username);
        message->setHasStatus(false);
        message->setHasUsername(true);
        return std::unique_ptr<AckMessage>(message);
    }
    else {
        byte ageArr[] = {byteVector[4],byteVector[5]};
        short age = bytesToShort(ageArr);
        byte postsArr[] = {byteVector[6],byteVector[7]};
        short posts = bytesToShort(postsArr);
        byte followersArr[] = {byteVector[8],byteVector[9]};
        short followers = bytesToShort(followersArr);
        byte followingArr[] = {byteVector[10],byteVector[11]};
        short following = bytesToShort(followingArr);
        message->setStat(age,posts,followers,following);
        message->setHasStatus(true);
        message->setHasUsername(false);
        return std::unique_ptr<AckMessage>(message);
    }

}

std::unique_ptr<ErrorMessage> EncoderDecoder::buildError() {
    auto * message = new ErrorMessage();
    byte opArr[] = {byteVector[2],byteVector[3]};
    short op = bytesToShort(opArr);
    message->setErrorOp(op);
    return std::unique_ptr<ErrorMessage>(message);
}

std::string EncoderDecoder::generateString(unsigned int j) {
    std::string out="";
    for(unsigned int i= j ;i<byteVector.size();i++){
        if(byteVector[i]=='\0')return out;
        out+=byteVector[i];
    }
    return out;
}

