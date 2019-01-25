#ifndef SERVER_USER_H
#define SERVER_USER_H

#include <iostream>
#include <list>
#include <mutex>
#include "tag.h"

using namespace std;

class User {
    /**
     * Nick użytkownika.
     */
    string nick;

    /**
     * Hasło użytkownika.
     */
    string password;

    /**
     * Lista tagów użytkownika.
     */
    list<Tag *> sublist;

    /**
     * Mutex na tworzenie nowych obiektów klasy User.
     */
    static mutex creating;

    /**
     * Statyczna lista zawierająca wszystkich utworzonych użytkownikow.
     */
    static list<User *> userlist;

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
     * Sprawdza, czy użutkownik subuje dany tag.
     * @return true - tak; false - nie
     */
    bool check_sub(Tag *);

    /**
     * Dodanie tagu do listy subskrybowanych.
     */
    void add_sub(Tag *);

    /**
     * Usunięcie tagu z listy subskrybowanych.
     */
    void del_sub(Tag *);

    /**
     * Zwraca statyczną listę wskaźników na wszystkich utworzonych użytkowników.
     * @return lista utworzonych użytkowników
     */
    static list<User *> get_userlist();

    /**
     * Zwraca wskaźnik na określonego nazwą użytkownika
     * @return wskaźnik na użytkownika
     */
    static User *get_user(string);
};


#endif //SERVER_USER_H
