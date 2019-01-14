#include "publication.h"

Publication::Publication(Tag *t, string s, User *u, string c) {
    tag = t;
    title = s;
    author = u;
    time (&date);
    content = c;
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
    return ctime(&date);
}

string Publication::get_content() {
    return content;
}
