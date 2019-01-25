#ifndef SERVER_TAG_H
#define SERVER_TAG_H

#include <iostream>
#include <list>
#include <mutex>

using namespace std;

class Tag {
    /**
     * Nazwa tagu.
     */
    string tagname;

    /**
     * Statyczna lista zawierjąca wszystkie utworzone tagi.
     */
    static list<Tag *> taglist;

    /**
     * Mutex na tworzenie nowych obiektów klasy Tag.
     */
    static mutex creating;

public:
    explicit Tag(string);

    /**
     * Pobranie nazwy tagu.
     * @return nazwa tagu
     */
    string get_tagname();

    /**
     * Zwraca statyczną listę wskaźników na wszystkie dostepne tagi.
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
