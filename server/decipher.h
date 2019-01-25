#ifndef SERVER_DECIPHER_H
#define SERVER_DECIPHER_H

#include <iostream>
#include <list>
#include "connection.h"
#include "publication.h"
#include "user.h"

using namespace std;

class Decipher {

    /**
     * Funkcja odcina nastepne pole komunikatu i zwraca je.
     * Oryginalny string ulega zmianie.
     * @return następne pole komunikatu
     */
    static string next(string &);

    /**
     * Funkcja odcina nastepne pole komunikatu o okreslonej długości i zwraca je.
     * Oryginalny string ulega zmianie.
     * @return następne pole komunikatu
     */
    static string next_l(string &);

    /**
     * Sprawdza poprawność zakończenia komunikatu.
     * @return 1 = prawidłowy koniec; else 0
     */
    static bool last(string &);

    /**
     * Pirwszy komunikat przesłany przez klienta do serwera; służy synchronizacji pracy.
     */
    static void a_join(string, Connection *);

    /**
     * Logowanie i rejestracja użytkownika (w zależności, czy użytkownik o danje nazwie już istnieje).
     */
    static void a_log(string, Connection *);

    /**
     * Dodanie tagu do subskrybowanych.
     */
    static void a_sub_add(string, Connection *);

    /**
     * Usunięcie tagu ze subskrybowanych.
     */
    static void a_sub_del(string, Connection *);

    /**
     * Wysłanie publikacji przez użytkownika.
     */
    static void a_send(string, Connection *);

    /**
     * Prośba o zakończnie pracy przez użytkownika.
     */
    static void a_term(string, Connection *);

public:
    /**
     * Dokonuje analizy składni komunikatu i wywyłuje odpowiednie metody.
     */
    static void study(string, Connection *);
};


#endif //SERVER_DECIPHER_H
