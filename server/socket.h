#ifndef SK_SERVER_SOCKET_H
#define SK_SERVER_SOCKET_H

#include <netinet/in.h>

class Socket {
    int server_port;
    int server_socket_descriptor;
    int bind_result;
    int listen_result;
    char reuse_addr_val;
    struct sockaddr_in server_address;

    /**
     * inicjacja
     */
    void s_initiation();

    /**
     * utworzenie gniazda
     */
    void s_creation();

    /**
     * dowiązanie / rejestracja usługi w systemie
     */
    void s_link();

    /**
     * nasłuchiwanie
     */
    void s_listen();

public:

    explicit Socket(int);

    ~Socket();

    /**
     * Pobranie deskryptora gniazda serwera.
     * @return deksryptor gniazda serwera
     */
    int s_get_server_socket_descriptor();
};


#endif //SK_SERVER_SOCKET_H
