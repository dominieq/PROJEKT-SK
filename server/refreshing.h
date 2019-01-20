#ifndef SERVER_REFRESHING_H
#define SERVER_REFRESHING_H

#include <iostream>
#include "publication.h"
#include "connection.h"

using namespace std;

class Refreshing {

    static string pubprepare(Publication *);

public:
    static void send_taglist(Connection *);

    static void send_sublist(Connection *);

    static void publishing(Connection *);

    static void publishing(Connection *, Tag *);

    static void publishing(Publication *);
public:
    static void refreshing();

};


#endif //SERVER_REFRESHING_H
