#include <iostream>
#include "tag.h"
#include "user.h"

using namespace std;

int main() {
    cout << "Hello, World!" << endl;

////    TESTY!!
//    Tag * t1 = new Tag("nowy1");
//    Tag * t2 = new Tag("nowy2");
//
//    User * u = new User("Wojtek", "haslo");
//
//    cout << u->get_nick() << endl;
//    u->add_sub(t1);
//    u->add_sub(t2);
//
//    cout << "Tagi:" << endl;
//    for (auto v : u->get_sublist()) {
//        cout << v->get_tagname() << endl;
//    }
//
//    string haslo;
//    do {
//        cout << endl << "Podaj hasło: ";
//        cin >> haslo;
//    }
//    while (!u->check_password(haslo));
//
//    u->del_sub(t1);
//    cout << "Tagi (po usunięciu \"" + t1->get_tagname() + "\"):" << endl;
//    for (auto v : u->get_sublist()) {
//        cout << v->get_tagname() << endl;
//    }
//
//    u->del_sub(t1);
//    cout << "Tagi (dalsze testy):" << endl;
//    for (auto v : u->get_sublist()) {
//        cout << v->get_tagname() << endl;
//    }
//
//    u->del_sub(t2);
//    cout << "Tagi (dalsze testy):" << endl;
//    for (auto v : u->get_sublist()) {
//        cout << v->get_tagname() << endl;
//    }
//
//    u->add_sub(t1);
//    cout << "Tagi (dalsze testy):" << endl;
//    for (auto v : u->get_sublist()) {
//        cout << v->get_tagname() << endl;
//    }

    return 0;
}
