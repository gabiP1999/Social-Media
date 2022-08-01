//
// Created by gabi4 on 07/01/2022.
//

#ifndef UNTITLED2_MESSAGEPRINTER_H
#define UNTITLED2_MESSAGEPRINTER_H
#include <mutex>
using mutex_lock = std::lock_guard<std::mutex>;

class MessagePrinter {
private:
    std::mutex lock;
public:
    MessagePrinter();
    void printLine(const std::string &line);
};


#endif //UNTITLED2_MESSAGEPRINTER_H
