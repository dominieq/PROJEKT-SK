#include "tag.h"

Tag::Tag(string name) {
    tagname = name;
}

string Tag::get_tagname() {
    return tagname;
}