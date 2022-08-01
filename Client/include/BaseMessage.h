//
// Created by gabi4 on 04/01/2022.
//

#ifndef UNTITLED2_BASEMESSAGE_H
#define UNTITLED2_BASEMESSAGE_H


#include <string>
#include <unordered_map>
#include <vector>

class BaseMessage {
protected:
    short type;
    std::vector<std::string> content;
public:
    virtual ~BaseMessage();
    BaseMessage();
    virtual short getType() const;
    void setType(short type);
    virtual std::vector<std::string> getContent() const;
    virtual void setContent(std::vector<std::string> content);
    virtual std::string toString();

};
class RegisterMessage : public BaseMessage{
public:
    virtual ~RegisterMessage();
    RegisterMessage();
};
class LoginMessage : public BaseMessage{
public:
    virtual ~LoginMessage();
    LoginMessage();
};
class LogoutMessage : public BaseMessage{
public:
    virtual ~LogoutMessage();
    LogoutMessage();
};
class FollowMessage : public BaseMessage{
public:
    virtual ~FollowMessage();
    FollowMessage();
};
class PublicMessage : public BaseMessage{
public:
    virtual ~PublicMessage();
    PublicMessage();
};
class PrivateMessage : public BaseMessage{
public:
    virtual ~PrivateMessage();
    PrivateMessage();
};
class LogstatMessage : public BaseMessage{
public:
    virtual ~LogstatMessage();
    LogstatMessage();
};
class StatusMessage : public BaseMessage{
public:
    virtual ~StatusMessage();
    StatusMessage();
};
class NotificationMessage : public BaseMessage{
private:
    bool isPrivate{};
public:
    virtual ~NotificationMessage();
    NotificationMessage();
    void setPrivate(bool b);
    bool getIsPrivate();
    std::string toString();
};
class AckMessage: public BaseMessage{
private:
    bool hasStatus;
    bool hasUserName;
    std::string username;
    short age;
    short numFollowers;
    short numFollowing;
    short numPosts;
    short Op;
public:
    virtual ~AckMessage();
    AckMessage();
    void setOp(short code);
    void setStat(short age,short posts,short followers,short following);
    void setUsername(std::string username);
    void setHasUsername(bool hasUsername);
    void setHasStatus(bool hasStatus);
    bool Status();
    bool Username();
    std::string getUsername()const;
    short getAge() const;
    short getPosts() const;
    short getFollowers() const;
    short getFollowing() const;
    short getOp() const;
    std::string toString();
};
class ErrorMessage : public BaseMessage{
private:
    short errorOp;
public:
    virtual ~ErrorMessage();
    ErrorMessage();
    void setErrorOp(short op);
    short getErrorOp();
    std::string toString();
};
class BlockMessage : public BaseMessage{
public:
    virtual ~BlockMessage();
    BlockMessage();
};
#endif //UNTITLED2_BASEMESSAGE_H
