#ifndef SERVER_WORKING_H
#define SERVER_WORKING_H

#include <iostream>
#include <thread>

using namespace std;

class Working {
    /**
     * Definiuje pracę funkcji heeding().
     */
    static bool active;

    /**
     * Metoda odpowiadajaca za przyłączanie nowych klientów.
     */
    static void heeding();

    /**
     * Funkcja tworząca tagi.
     */
    static void initialization();

public:
    /**
     * Utuchomienie serwera.
     */
    static void launch(int, char* []);

    /**
     * Inicjalizacja do pracy i działanie serwera.
     */
    static void operation();

    /**
     * Zamknięcie serwera.
     */
    static void abolish();
};


#endif //SERVER_WORKING_H
