#ifndef SERVER_WORKING_H
#define SERVER_WORKING_H

#include <iostream>
#include <thread>

using namespace std;

class Working {

    static bool active;

    static void heeding();

    static void initialization();

public:
    static void launch(int, char* []);

    static void operation();

    static void abolish();
};


#endif //SERVER_WORKING_H
