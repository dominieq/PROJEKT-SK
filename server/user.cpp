#include "user.h"

list<User *> User::userlist;

/**
 * Funkcja używana przy oczyszczaniu listy tagów z powtarzających się pozycji,
 * wykorzystywana w funkcji uniqe() na liście tagów.
 * @param first pierwszy sprawdzany tag
 * @param second drugi sprawdzany tag
 * @return 'false' = różne tagi; 'true' = identyczne tagi
 */
bool same (Tag *first, Tag *second) {
    return (first == second);
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
    bool nowy = true;
    for (auto *v : userlist) {
        if (v->get_nick() == n) {
            nowy = false;
            break;
        }
    }
    if (nowy) {
        nick = n;
        password = p;
        userlist.push_back(this);
    } else {
        delete this;
    }
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

bool User::check_sub(Tag *t) {
    for (auto *v : sublist) {
        if (v == t) {
            return true;
        }
    }
    return false;
}

void User::del_sub(Tag *usun) {
    for (auto it = sublist.begin(); it != sublist.end(); /*  */ ) {
        if (same(*it, usun)) {
            it = sublist.erase(it);
        } else {
            ++it;
        }
    }
}

list <User *> User::get_userlist() {
    return userlist;
}

User *User::get_user(string u) {
    for (auto *v : User::get_userlist()) {
        if (v->get_nick() == u) {
            return v;
        }
    }
    return nullptr;
}