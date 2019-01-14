#ifndef SERVER_PUBLICATION_H
#define SERVER_PUBLICATION_H


#include <iostream>
#include "tag.h"
#include "user.h"

using namespace std;

class Publication {
    Tag *tag;
    string title;
    User *author;
    time_t date;
    string content;

public:
    Publication(Tag *, string, User *, string);

    /**
     * Zwraca tag publikacji.
     * @return tag
     */
    Tag *get_tag();

    /**
     * Zwraca tytuł publikacji.
     * @return tutył
     */
    string get_title();

    /**
     * Zwraca autora publikacji.
     * @return użytkownik będący autorem publikacji
     */
    User *get_author();

    /**
     * Zwraca skonwertowaną datę publikacji.
     * @return data publikacji
     */
    string get_date_s();

    /**
     * Zwraca treść publikacji.
     * @return treść
     */
    string get_content();

};


#endif //SERVER_PUBLICATION_H
