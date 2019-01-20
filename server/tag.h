#ifndef SERVER_TAG_H
#define SERVER_TAG_H

#include <iostream>
#include <list>

using namespace std;

class Tag {
    /**
     * nazwa tagu
     */
    string tagname;

    /**
     * statyczna lista zawierjąca wszystkie utworzone tagi
     */
    static list<Tag *> taglist;

public:
    Tag(string);

    /**
     * Pobranie nazwy tagu.
     * @return nazwa tagu
     */
    string get_tagname();

    /**
     * Zwraca statyczną listę wskaźników na wszystkich dostepnych tagów.
     * @return lista dostępnych tagów
     */
    static list<Tag *> get_taglist();

    /**
     * Zwraca wskaźnik na określony nazwą tag.
     * @return wskaźnik na tag
     */
    static Tag *get_tag(string);
};


#endif //SERVER_TAG_H
