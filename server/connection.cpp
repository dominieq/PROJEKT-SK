#include "connection.h"
#include "decipher.h"
#include <unistd.h>
#include <sys/socket.h>
#include <stdexcept>


/*
 * Inicjalizacja statycznych.
 */
list<Connection *> Connection::connectionlist;
mutex Connection::creating;

Connection::Connection(int server_socket_descriptor) {
    creating.lock();
    s_accept(server_socket_descriptor);
    if (connectionlist.size() >= CONNECTIONS_LIMIT) {
        cout << "Nadmiarowy client." << endl;
        active = false;
        delete this;
    } else {
        active = true;
        connectionlist.push_back(this);
        cout << "OK: Połączono klienta." << endl;
    }

    creating.unlock();
}

Connection::~Connection() {
    for(auto it = connectionlist.begin(); it != connectionlist.end(); /*  */ ) {
        if (*it == this) {
            it = connectionlist.erase(it);
            break;
        } else {
            ++it;
        }
    }

    close(connection_socket_descriptor);
    cout << "OK: Zamknięcie połączenia csd: " << connection_socket_descriptor << endl;
}

void Connection::s_accept(int server_socket_descriptor) {
    cout << "OK: Serwer czeka na klienta." << endl;
    connection_socket_descriptor = accept(server_socket_descriptor, nullptr, nullptr);
    if (connection_socket_descriptor < 0) {
        cout << stderr << ": Błąd przy próbie utworzenia gniazda dla połączenia." << endl;
        exit(2);
    }
}

int Connection::s_get_connection_socket_descriptor() {
    return connection_socket_descriptor;
}

void Connection::s_read() {
    bool poczatek;
    ssize_t ile =  0;
    char znak[1];
    string s_ile, s_tresc;

    cout << "start" << endl;

    try {
        while (active) {
            poczatek = true;
            s_ile.clear();
            while (poczatek) {
                if (read(connection_socket_descriptor, znak, 1) > 0) {
                    if (znak[0] == ';') {
                        try {
                            ile = stoi(s_ile);
                        } catch (invalid_argument &e) {
                            throw (runtime_error("błąd w długości wiadomości"));
                        }
                        poczatek = false;
                    } else if ((znak[0] < '0' || znak[0] > '9') && znak[0] != '\n') {
                        throw runtime_error("błąd w długości wiadomości");
                    } else {
                        s_ile+=znak[0];
                        if (s_ile.size() > to_string(MAX_SIZE).size()+1) {
                            cout << "errr " << s_ile << " " << s_ile.size() << " " << to_string(MAX_SIZE).size() << endl;
                            throw runtime_error("spodziewana za długa wiadomość");
                        }
                    }
                } else {
                    throw runtime_error("zerwane polaczenie");
                }
            }

            if (ile > MAX_SIZE) {
                throw runtime_error("za długa wiadomość");
            }

            s_tresc.clear();
            while (ile--) {
                if (read(connection_socket_descriptor, znak, 1) > 0) {
                    if (znak[0] != '\n' && znak[0] != '\0') {
                        s_tresc+=znak[0];
                    } else {
                        ile++;
                    }
                } else {
                    throw runtime_error("zerwane polaczenie");
                }
            }

            s_tresc.append("\0");
            cout << s_tresc << endl;
            try {
                Decipher::study(s_tresc, this);
            } catch (logic_error &e) {
                throw runtime_error("długość pola");
            }

        }
    } catch (runtime_error &e) {
        cout << "!!: Klient zerwał połączenie. csd: " << this->s_get_connection_socket_descriptor() << " powód: " << e.what() << endl;
        this->disable();
    } catch (...) {
        this->disable();
    }
}

void Connection::s_write(string tresc) {
    send.lock();
    string to_send;
    to_send += to_string(tresc.size());
    to_send += ';';
    to_send += tresc;
    write(connection_socket_descriptor, to_send.c_str(), to_send.size());

    cout << "Wysłano: " << to_send << endl; //TODO testowanie
    send.unlock();
}

void Connection::disable() {
    active = false;
    delete this;
}

list<Connection *> Connection::get_connectionlist() {
    return connectionlist;
}

void Connection::assign(User *u) {
    online = u;
}

User *Connection::get_user() {
    return online;
}
