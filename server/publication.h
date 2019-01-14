#ifndef SERVER_PUBLICATION_H
#define SERVER_PUBLICATION_H


#include <iostream>
#include <list>
#include "tag.h"
#include "user.h"

using namespace std;

class Publication {
    Tag *tag;
    string title;
    User *author;
    time_t date;
    string content;

    /**
     * statyczna lista zawierająca wszystkie publikacje
     */
    static list<Publication *> publicationlist;

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

    /**
     * Zwraca statyczną listę wskaźników na wszystkie publikacje.
     * @return lista publikacji
     */
    static list<Publication *> get_publicationlist();

    //TODO
    static list<Publication *> get_publicationlist(Tag *);

};


#endif //SERVER_PUBLICATION_H
