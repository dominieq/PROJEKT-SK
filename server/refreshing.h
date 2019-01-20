#ifndef SERVER_REFRESHING_H
#define SERVER_REFRESHING_H

#include <iostream>
#include "publication.h"
#include "connection.h"

using namespace std;

class Refreshing {

    static string pubprepare(Publication *);

    static void send_taglist(Connection *);

    static void send_sublist(Connection *);

    static void publishing(Connection *);
public:
    static void refreshing();

};


#endif //SERVER_REFRESHING_H
