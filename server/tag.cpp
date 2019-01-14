#include "tag.h"

list<Tag *> Tag::taglist;

Tag::Tag(string name) {
    bool nowy = true;
    for (auto v : taglist) {
        if (v->get_tagname() == name) {
            nowy = false;
            break;
        }
    }
    if (nowy) {
        tagname = name;
        taglist.push_back(this);
    } else {
        delete this;
    }
}

string Tag::get_tagname() {
    return tagname;
}

list<Tag *> Tag::get_taglist() {
    return taglist;
}

Tag *Tag::get_tag(string t) {
    for (auto v : Tag::get_taglist()) {
        if (v->get_tagname() == t) {
            return v;
        }
    }
    return nullptr;
}