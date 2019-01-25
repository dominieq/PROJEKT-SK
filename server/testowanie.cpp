#include "testowanie.h"
#include "tag.h"
#include "user.h"
#include "publication.h"
#include <iostream>
#include <thread>
#include <unistd.h>
#include <fstream>

using namespace std;

void Testowanie::wysyp(int ile) {
    cout << "start " << User::get_userlist().size() << endl;

    thread t_0(wysyp1, "testowy:", ile);
    t_0.join();

    cout << "po: " << User::get_userlist().size() << endl;

    sleep(10);

    cout << "po: " << User::get_userlist().size() << endl;

    sleep(10);

    cout << "po: " << User::get_userlist().size() << endl;



    sleep(10);

    cout << "po: " << User::get_userlist().size() << endl;

    sleep(10);

    cout << "po: " << User::get_userlist().size() << endl;

    sleep(10);

    cout << "prawdopodobnie ostatni przy <30 : " << User::get_userlist().size() << endl;



    fstream plik;

    plik.open("testowanie.txt", ios::out);

    for (auto *v : User::get_userlist()) {
        plik << v->get_nick() << endl;
    }

    plik.close();
}

void Testowanie::wysyp1(string n, int ile) {
    for (int i = 0; i < ile; i++) {
        thread t1(wysyp2, n + "_" + to_string(i), ile);
        t1.detach();
    }
}

void Testowanie::wysyp2(string n2, int ile) {
    for (int i = 0; i < ile; i++) {
        thread t2(wysyp3, n2 + "_" + to_string(i), ile);
        t2.detach();
    }
}

void Testowanie::wysyp3(string n3, int ile) {
    for (int i = 0; i < ile; i++) {
        new User(n3 + "_" + to_string(i),"");
    }

    if (n3 == "testowy:_" + to_string(ile-1) + "_" + to_string(ile-1)) {
        cout << "już: " << User::get_userlist().size() << endl;
    }
}

void Testowanie::test() {
    Tag *t1 = new Tag("example1");
    Tag *t2 = new Tag("example2");

    User *u1 = new User("tester1", "tester1");
    User *u2 = new User("tester2", "");

    new Publication(t1, "test1", u1, "treść testu");

    new Publication(t2, "test2", u2, "treść testu");

    u2->add_sub(t2);

//    int ile = 30;
//    cout << "Sp: " << ile*ile*ile << endl;
//    Testowanie::wysyp(ile);

    cout << User::get_userlist().size() << endl;

    new User("tester x", "t");
    new User("tester x", "t");

    cout << User::get_userlist().size() << endl;




}
