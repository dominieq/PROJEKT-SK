#include "tag.h"

list<Tag *> Tag::taglist;
mutex Tag::creating;

Tag::Tag(string name) {
    creating.lock();
    bool nowy = true;
    for (auto *v : taglist) {
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
    creating.unlock();
}

string Tag::get_tagname() {
    return tagname;
}

list<Tag *> Tag::get_taglist() {
    return taglist;
}

Tag *Tag::get_tag(string t) {
    for (auto *v : Tag::get_taglist()) {
        if (v->get_tagname() == t) {
            return v;
        }
    }
    return nullptr;
}