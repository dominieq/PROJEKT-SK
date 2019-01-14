#ifndef SERVER_TAG_H
#define SERVER_TAG_H

#include <iostream>

using namespace std;

class Tag {
    string tagname;

public:
    Tag(string);

    /**
     * Pobranie nazwy tagu.
     * @return nazwa tagu
     */
    string get_tagname();
};


#endif //SERVER_TAG_H
