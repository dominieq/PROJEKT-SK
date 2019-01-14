#include <iostream>
#include "socket.h"

using namespace std;

int main(int argc, char* argv[]) {
    cout << "Hello, World!" << endl;

    if (argc != 2 || atoi(argv[1]) < 2000 || atoi(argv[1]) > 65535) {
        cout << "Sposób użycia: " << argv[0] << " port_number (2000:65535)." << endl;
        exit(1);
    }

    Socket * server_socket = new Socket(atoi(argv[1]));

    delete server_socket;


    return 0;
}