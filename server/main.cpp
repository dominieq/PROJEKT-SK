#include <iostream>
#include "connection.h"
#include "socket.h"
#include "tag.h"
#include "user.h"
#include "publication.h"

using namespace std;
int main(int argc, char* argv[]) {
    cout << "Hello, World!" << endl;

    if (argc != 2 || atoi(argv[1]) < 2000 || atoi(argv[1]) > 65535) {
        cout << "Sposób użycia: " << argv[0] << " port_number (2000:65535)." << endl;
        exit(1);
    }

////TESTY
//    new Tag("nowy1");
//    new Tag("nowy2");
//    new Tag("nowy1");
//
//    User * u = new User("Wojtek", "haslo");
//    User * u2 = new User("testowy", "");
//
//    cout << "ilu: " << User::get_userlist().size() << endl;
//
//    new User("Kasia", "");
//    cout << "ilu: " << User::get_userlist().size() << endl;
//
//    User * k = new User("Wojtek", "");
//    cout << "ilu: " << User::get_userlist().size() << endl;
//
//    Publication * p = new Publication(Tag::get_tag("nowy2"), "tyt", u, "pierwszy testowy ciekawy świata");
//
//    cout << p->get_tag()->get_tagname() << endl;
//    cout << p->get_title() << endl;
//    cout << p->get_author()->get_nick() << endl;
//    cout << p->get_date_s() << endl;
//    cout << p->get_content() << endl;
//
//    cout << p->get_content().size() << endl;
//
//    for (auto v : Tag::get_taglist()) {
//        cout << v->get_tagname() << endl;
//    }
//
//    new Publication(Tag::get_tag("nowy1"), "tyt", u, "drugi testowy ciekawy świata");
//
//    cout << Publication::get_publicationlist().size() << endl;
//    cout << Publication::get_publicationlist(Tag::get_tag("nowy1")).size() << endl;
//    cout << Publication::get_publicationlist(Tag::get_tag("nowy2")).size() << endl;
//    cout << endl << endl;
//    new Publication(Tag::get_tag("nowy2"), "tyt", u, "trzeci testowy ciekawy świata");
//
//    cout << Publication::get_publicationlist().size() << endl;
//    cout << Publication::get_publicationlist(Tag::get_tag("nowy1")).size() << endl;
//    cout << Publication::get_publicationlist(Tag::get_tag("nowy2")).size() << endl;
//    cout << endl << endl;
//
//
//
//    cout << "ADD TAG TEST" << endl;
//    u->add_sub(Tag::get_tag("nowy1"));
//    u->add_sub(Tag::get_tag("nowy1"));
//    u->add_sub(Tag::get_tag("nowy1"));
//    cout << u->get_sublist().size() << endl;
//    u->add_sub(Tag::get_tag("nowy2"));
//    u->add_sub(Tag::get_tag("nowy1"));
//    u->add_sub(Tag::get_tag("nowy1"));
//    cout << u->get_sublist().size() << endl;
//    u->add_sub(Tag::get_tag("nowy1"));
//    u->add_sub(Tag::get_tag("nowy1"));
//    u->del_sub(Tag::get_tag("nowy1"));
//    cout << u->get_sublist().size() << endl;


//    cout << t3->get_tagname() << endl;

//    Socket * server_socket = new Socket(atoi(argv[1]));
//
//    Connection * conn = new Connection(server_socket->s_get_server_socket_descriptor());
//
//    conn->s_read();
//
//    conn->s_write("testowy\0");
//
//    delete conn;
//
//    delete server_socket;


    return 0;
}
