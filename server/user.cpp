#include "user.h"

/**
 * Funkcja używana przy oczyszczaniu listy tagów z powtarzających się pozycji,
 * wykorzystywana w funkcji uniqe() na liście tagów. Porównywane są nazwy tagów.
 * @param first pierwszy sprawdzany tag
 * @param second drugi sprawdzany tag
 * @return 'false' = różne tagi; 'true' = identyczne tagi
 */
bool same (Tag *first, Tag *second) {
    return (first->get_tagname() == second->get_tagname());
}

/**
 * Funkcja używana przy sortowaniu listy tagów, wykorzystywana w funkcji sort() na liście tagów.
 * Porównywane są nazwy tagów.
 * @param first pierwszy sprawdzany tag
 * @param second drugi sprawdzany tag
 * @return 'false' = pierwszy starszy; 'true' = pierwszy młodszy
 */
bool order (Tag *first, Tag *second) {
    return (first->get_tagname() < second->get_tagname());
}

User::User(string n, string p) {
    nick = n;
    password = p;
}

string User::get_nick() {
    return nick;
}

bool User::check_password(string p) {
    if (password == p) {
        return true;
    } else {
        return false;
    }
}

list<Tag *> User::get_sublist() {
    return sublist;
}

void User::add_sub(Tag *nowy) {
    sublist.push_back(nowy);
    sublist.sort(order);
    sublist.unique(same);
}

void User::del_sub(Tag *usun) {
    for(list<Tag*>::iterator it = sublist.begin(); it != sublist.end(); /*  */ ) {
        if (same(*it, usun)) {
            it = sublist.erase(it);
        } else {
            ++it;
        }
    }
}
