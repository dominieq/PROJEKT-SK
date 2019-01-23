#include "publication.h"
#include <utility>

list<Publication *> Publication::publicationlist;

Publication::Publication(Tag *t, string s, User *u, string c) {
    tag = t;
    title = move(s);
    author = u;
    time (&date);
    content = move(c);

    publicationlist.push_back(this);
}

Tag *Publication::get_tag() {
    return tag;
}

string Publication::get_title() {
    return title;
}

User *Publication::get_author() {
    return author;
}

string Publication::get_date_s() {
    string edited = ctime(&date);
    edited = edited.substr(0, edited.size()-1);
    return edited;
}

string Publication::get_content() {
    return content;
}

list<Publication *> Publication::get_publicationlist() {
    return publicationlist;
}

list<Publication *> Publication::get_publicationlist(Tag *t) {
    list<Publication *> specified;
    for (auto *v : Publication::get_publicationlist()) {
        if (v->get_tag() == t) {
            specified.push_back(v);
        }
    }
    return specified;
}
