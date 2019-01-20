cmake_minimum_required(VERSION 3.13)
project(server)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -pthread")

add_executable(server main.cpp socket.cpp socket.h connection.cpp connection.h tag.cpp tag.h user.cpp user.h publication.cpp publication.h working.cpp working.h decipher.cpp decipher.h testowanie.cpp testowanie.h refreshing.cpp refreshing.h)