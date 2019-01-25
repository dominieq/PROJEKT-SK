#include "working.h"
#include "socket.h"
#include "connection.h"
#include "testowanie.h"
//#include "refreshing.h"
#include <iostream>

using namespace std;

static Socket *server_socket;
bool Working::active = true; //TODO work?

void Working::launch(int argc, char* argv[]) {
    cout << "----------" << " START " << "----------" << endl;
    if (argc != 2 || atoi(argv[1]) < 2000 || atoi(argv[1]) > 65535) {
        cout << "Sposób użycia: " << argv[0] << " port_number (2000:65535)." << endl;
        exit(1);
    }

    server_socket = new Socket (atoi(argv[1]));
}

void Working::operation() {
    thread t_init(Working::initialization);

    t_init.detach();

    thread t_heeding(Working::heeding);

    t_heeding.detach();

}

void Working::abolish() {
    active = false;
    for (auto v : Connection::get_connectionlist()) {
        delete v;
    }

    delete server_socket;

    cout << "----------" << " STOP " << "-----------" << endl;
}

void Working::heeding() {
    cout << "OK: Rozpoczęnie nasłuchiwania." << endl;
    while (active) {
        thread t(&Connection::s_read, new Connection(server_socket->s_get_server_socket_descriptor()));
        t.detach();
    }
}

void Working::initialization() {

    new Tag("news");
    new Tag("music");
    new Tag("movies");
    new Tag("art");

    new Tag("games");
    new Tag("events");
    new Tag("cats");
    new Tag("dogs");
    
//    Testowanie::test();
}