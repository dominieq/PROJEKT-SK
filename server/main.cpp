#include <iostream>

using namespace std;

int main(int argc, char* argv[]) {
    cout << "Hello, World!" << endl;

    if (argc != 2 || atoi(argv[1]) < 2000 || atoi(argv[1]) > 65535) {
        cout << "Sposób użycia: " << argv[0] << " port_number (2000:65535)." << endl;
        exit(1);
    }

    Socket * server_socket = new Socket(atoi(argv[1]));

    Connection * conn = new Connection(server_socket->s_get_server_socket_descriptor());

    conn->s_read();

    conn->s_write("testowy\0");

    delete conn;

    delete server_socket;


    return 0;
}
#include "connection.h"
#include "socket.h"