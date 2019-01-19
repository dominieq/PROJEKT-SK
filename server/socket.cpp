#include <iostream>
#include <unistd.h>     //close()
#include <string.h>     //memset()
#include "socket.h"

using namespace std;

#define QUEUE_SIZE 10

Socket::Socket(int port) {
    server_port = port;
    reuse_addr_val = 1;

    s_initiation();
    s_creation();
    s_link();
    s_listen();

    cout << "OK: Utworzono socket serwera." << endl;
}

Socket::~Socket(){
    close(server_socket_descriptor);
    cout << "OK: Zamknięto socket serwera." << endl;
}

void Socket::s_initiation() {
    memset(&server_address, 0, sizeof(struct sockaddr));
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = htonl(INADDR_ANY);
    server_address.sin_port = htons(server_port);
}

void Socket::s_creation() {
    server_socket_descriptor = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket_descriptor < 0) {
        cout << stderr << ": Błąd przy próbie utworzenia gniazda." << endl;
        exit(1);
    }
}

void Socket::s_link() {
    setsockopt(server_socket_descriptor, SOL_SOCKET, SO_REUSEADDR,
               (char*)&reuse_addr_val, sizeof(reuse_addr_val));
    bind_result = bind(server_socket_descriptor, (struct sockaddr*)&server_address,
                       sizeof(struct sockaddr));
    if (bind_result < 0) {
        cout << stderr << ": Błąd przy próbie dowiązania adresu IP i numeru portu do gniazda." << endl;
        exit(1);
    }
}

void Socket::s_listen() {
    listen_result = listen(server_socket_descriptor, QUEUE_SIZE);
    if (listen_result < 0) {
        cout << stderr << ": Błąd przy próbie ustawienia wielkości kolejki." << endl;
        exit(1);
    }
}

int Socket::s_get_server_socket_descriptor() {
    return server_socket_descriptor;
}