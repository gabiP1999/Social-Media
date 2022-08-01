LDFLAGS:=-lboost_system -pthread

all: link

link: compile
	@echo "Linking Object"
	g++ -o bin/Client bin/main.o bin/MessagePrinter.o bin/EncoderDecoder.o bin/ConnectionHandler.o bin/BGSConnectionHandler.o bin/BaseMessage.o $(LDFLAGS)

compile: src/main.cpp src/MessagePrinter.cpp src/EncoderDecoder.cpp src/ConnectionHandler.cpp src/BGSConnectionHandler.cpp src/BaseMessage.cpp
	@echo "Compiling sourse code"
	g++ -g -c -Wall -Weffc++ -std=c++11 -o bin/main.o src/main.cpp 
	g++ -g -c -Wall -Weffc++ -std=c++11 -o bin/MessagePrinter.o src/MessagePrinter.cpp
	g++ -g -c -Wall -Weffc++ -std=c++11 -o bin/EncoderDecoder.o src/EncoderDecoder.cpp
	g++ -g -c -Wall -Weffc++ -std=c++11 -o bin/ConnectionHandler.o src/ConnectionHandler.cpp
	g++ -g -c -Wall -Weffc++ -std=c++11 -o bin/BGSConnectionHandler.o src/BGSConnectionHandler.cpp
	g++ -g -c -Wall -Weffc++ -std=c++11 -o bin/BaseMessage.o src/BaseMessage.cpp
clean:
	@echo "cleaning bin direoctory"
	rm -rf bin/*
