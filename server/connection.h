#ifndef SERVER_CONNECTION_H
#define SERVER_CONNECTION_H

#include <iostream>
#include <list>
#include <mutex>
#include "user.h"

using namespace std;

#define BUF_SIZE 1024
#define CONNECTIONS_LIMIT 3

class Connection {
    int connection_socket_descriptor;
    char buffer [BUF_SIZE];
//    int dlugosc;
    ssize_t dlugosc;

    bool active;
    User *online;
    static mutex creating;
    mutex send;


    void s_accept(int);

    /**
     * statyczna lista zawierjąca wszystkie aktywne połączenia
     */
    static list<Connection *> connectionlist;

public:
    explicit Connection(int);

    ~Connection();

    /**
     * Pobranie deskryptora gniazda połączenia.
     * @return deskryptora gniazda połączenia
     */
    int s_get_connection_socket_descriptor();

    /**
     * Odczytanie i wypisanie komunikatu od klienta.
     */
    void s_read();

    /**
     * Wysłanie komunikatu do klienta.
     */
    void s_write(string);

    /**
     * Wyłącznie działania połączenia.
     */
    void disable();

    /**
     * Przypisuje użytkownika do połączenia po zalogowaniu.
     */
    void assign(User *);

    /**
     * Zwraca wskaźnik na zalogowanego użytkownika dla aktualnego połączenia.
     * @return użytkownik
     */
    User *get_user();

    /**
     * Zwraca statyczną listę wskaźników na wszystkie aktywne połączenia.
     * @return lista aktywnych połączeń
     */
    static list<Connection *> get_connectionlist();
};


#endif //SERVER_CONNECTION_H
