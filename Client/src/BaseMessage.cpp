//
// Created by gabi4 on 04/01/2022.
//

#include "../include/BaseMessage.h"

BaseMessage::BaseMessage():type((short)0), content(){}
BaseMessage::~BaseMessage() = default;


short BaseMessage::getType() const {
    return type;
}

void BaseMessage::setType(short type) {
    this->type = type;
}


void BaseMessage::setContent(std::vector<std::string> content) {
    this->content=content;
}

std::vector<std::string> BaseMessage::getContent() const {
    return  content;
}

std::string BaseMessage::toString() {
    return "BaseMessage!!";
}

void AckMessage::setOp(short code) {
this->Op=code;
}

short AckMessage::getOp() const {
    return Op;
}

void AckMessage::setStat(short age, short posts, short followers, short following) {
this->age=age;this->numPosts=posts;this->numFollowers=followers;this->numFollowing=following;
}

void AckMessage::setUsername(std::string username) {
this->username=username;
}

void AckMessage::setHasStatus(bool hasStatus) {
this->hasStatus=hasStatus;
}

bool AckMessage::Status() {
    return hasStatus;
}

bool AckMessage::Username(){
    return hasUserName;
}

std::string AckMessage::getUsername() const {
    return username;
}

short AckMessage::getAge() const {
    return age;
}

short AckMessage::getPosts() const {
    return numPosts;
}

short AckMessage::getFollowers() const {
    return numFollowers;
}

short AckMessage::getFollowing() const {
    return numFollowing;
}

void AckMessage::setHasUsername(bool hasUsername) {
this->hasUserName=hasUsername;
}

std::string AckMessage::toString() {
    std::string out="ACK "+std::to_string(Op);
    if(hasUserName)out.append(" "+username);
    if(hasStatus)out.append(" "+std::to_string(age)+" "+std::to_string(numPosts)+" "+std::to_string(numFollowers)+" "+std::to_string(numFollowing));
    return out;
}

AckMessage::AckMessage():hasStatus(false),hasUserName(false),username(""),age((short)-1),numFollowers((short)-1),numFollowing((short)-1),numPosts((short)-1),Op((short)-1) {
    setType((short)10);
}

void NotificationMessage::setPrivate(bool b) {
isPrivate=b;
}

bool NotificationMessage::getIsPrivate() {
    return isPrivate;
}

std::string NotificationMessage::toString() {
    std::string out="NOTIFICATION ";
    if(isPrivate)out.append("Private ");
    else out.append("Public ");
    for(std::string s : getContent()){
        out.append(s+" ");
    }
    return out;
}

NotificationMessage::NotificationMessage() {
    setType((short)9);
}

void ErrorMessage::setErrorOp(short op) {
errorOp=op;
}

short ErrorMessage::getErrorOp() {
    return errorOp;
}

std::string ErrorMessage::toString() {
    return "ERROR "+std::to_string(errorOp);
}

ErrorMessage::ErrorMessage():errorOp(-1) {
    setType((short)11);
}

BlockMessage::BlockMessage() {
    setType((short)12);
}

StatusMessage::StatusMessage() {
    setType((short)8);
}

LogstatMessage::LogstatMessage() {
    setType((short)7);
}

PrivateMessage::PrivateMessage() {
    setType((short)6);
}

PublicMessage::PublicMessage() {
    setType((short)5);
}

FollowMessage::FollowMessage() {
    setType((short)4);
}

LogoutMessage::LogoutMessage() {
    setType((short)3);
}

LoginMessage::LoginMessage() {
    setType((short)2);
}

RegisterMessage::RegisterMessage() {
    setType((short)1);
}
RegisterMessage::~RegisterMessage(){}
LoginMessage::~LoginMessage(){}
LogoutMessage::~LogoutMessage(){}
FollowMessage::~FollowMessage(){}
PublicMessage::~PublicMessage(){}
PrivateMessage::~PrivateMessage(){}
LogstatMessage::~LogstatMessage(){}
StatusMessage::~StatusMessage(){}
NotificationMessage::~NotificationMessage(){}
ErrorMessage::~ErrorMessage(){}
AckMessage::~AckMessage(){}
BlockMessage::~BlockMessage(){}
