#ifndef SERVER_USER_H
#define SERVER_USER_H

#include <iostream>
#include <list>
#include "tag.h"

using namespace std;

class User {
    string nick;
    string password;

    list<Tag *> sublist;
public:
    void setSublist(const list<Tag *> &sublist);

public:
    User(string, string);

    /**
     * Pobranie nicku użytkownika
     * @return nikc użytkownika
     */
    string get_nick();

    /**
     * Sprawdzenie poprawności logowania użytkownika.
     * @return 1 = hasło prawidłowe; 0 = hasło nieprawidłowe
     */
    bool check_password(string);

    /**
     * Pobranie listy subskrybowanych tagów.
     * @return lista subskrybowanych tagów
     */
    list<Tag *> get_sublist();

    /**
     * Dodanie tagu do listy subskrybowanych.
     */
    void add_sub(Tag *);

    /**
     * Usunięcie tagu z listy subskrybowanych.
     */
    void del_sub(Tag *);

};


#endif //SERVER_USER_H
