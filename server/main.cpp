#include <iostream>
#include "tag.h"
#include "user.h"
#include "publication.h"

using namespace std;

int main() {
    cout << "Hello, World!" << endl;

////    TESTY!!
    Tag * t1 = new Tag("nowy1");
    Tag * t2 = new Tag("nowy2");

    User * u = new User("Wojtek", "haslo");

    Publication * p = new Publication(t1, "tyt", u, "pierwszy testowy ciekawy świata");

    cout << p->get_tag()->get_tagname() << endl;
    cout << p->get_title() << endl;
    cout << p->get_author()->get_nick() << endl;
    cout << p->get_date_s() << endl;
    cout << p->get_content() << endl;

    cout << p->get_content().size() << endl;
    

    return 0;
}
