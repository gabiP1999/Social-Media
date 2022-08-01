//
// Created by gabi4 on 07/01/2022.
//

#include <iostream>
#include "../include/MessagePrinter.h"

MessagePrinter::MessagePrinter() : lock() { }



void MessagePrinter::printLine(const std::string &line) {
        mutex_lock _lock(lock);
        std::cout << line << std::endl;
}
