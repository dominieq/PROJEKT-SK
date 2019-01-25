#ifndef SERVER_REFRESHING_H
#define SERVER_REFRESHING_H

#include <iostream>
#include "publication.h"
#include "connection.h"

using namespace std;

class Refreshing {

    /**
     * Wewnętrzna metoda pomagająca przy przygotowaniu publikacji do wysłania.
     * @return
     */
    static string pubprepare(Publication *);

public:
    /**
     * Wysłanie połączonemu użytkownikowi listy wszystkich tagów.
     */
    static void send_taglist(Connection *);

    /**
     * Wysłanie połączonemu użytkownikowi listy subskrybowanych tagów.
     */
    static void send_sublist(Connection *);

    /**
     * Wysłanie wszystkich publikacji o subskrybowanych tagach do połączonego użytkownika.
     */
    static void publishing(Connection *);

    /**
     * Wysłanie wszystkich publikacji o zadanym tagu do połączonego użytkownika.
     */
    static void publishing(Connection *, Tag *);

    /**
     * Wysłanie nowej publikacji do użytkowników subskrybujący jej tag.
     */
    static void publishing(Publication *);
};


#endif //SERVER_REFRESHING_H
