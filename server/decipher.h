#ifndef SERVER_DECIPHER_H
#define SERVER_DECIPHER_H

#include <iostream>
#include <list>
#include "connection.h"
#include "publication.h"
#include "user.h"

using namespace std;

class Decipher {

public: //TODO
    static string part(string, unsigned int);
    static void a_join(string, Connection *);
    static void a_join_new(string, Connection *);
    static void a_join_old(string, Connection *);
    static void a_sub(string, Connection *);
    static void a_send_pub(string, Connection *);
    static void a_term(string, Connection *);

public:
    static void study(string, Connection *);
};


#endif //SERVER_DECIPHER_H
