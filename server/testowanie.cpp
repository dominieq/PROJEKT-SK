#include "testowanie.h"
#include "tag.h"
#include "user.h"
#include "publication.h"
#include <iostream>

using namespace std;

void Testowanie::test() {
    Tag *t1 = new Tag("example1");
    Tag *t2 = new Tag("example2");

    User *u1 = new User("tester1", "tester1");
    User *u2 = new User("tester2", "");

    new Publication(t1, "test1", u1, "treść testu");

    new Publication(t2, "test2", u2, "treść testu");

    u2->add_sub(t2);

}
