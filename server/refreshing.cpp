#include <unistd.h>
#include "refreshing.h"

string Refreshing::pubprepare(Publication *p) {
    string pub;
    pub.append("PUB;");
    pub.append(p->get_tag()->get_tagname());
    pub.append(";");
    pub.append(to_string(p->get_title().size()));
    pub.append(";");
    pub.append(p->get_title());
    pub.append(";");
    pub.append(to_string(p->get_author()->get_nick().size()));
    pub.append(";");
    pub.append(p->get_author()->get_nick());
    pub.append(";");
    pub.append(p->get_date_s());
    pub.append(";");
    pub.append(to_string(p->get_content().size()));
    pub.append(";");
    pub.append(p->get_content());
    pub.append(";END");
    return pub;
}


void Refreshing::send_taglist(Connection *conn) {
    ssize_t ile =  Tag::get_taglist().size();
    string tags = "TAG;";
    if (!Tag::get_taglist().empty()) {
        for (auto *v : Tag::get_taglist()) {
            tags.append(v->get_tagname());
            if (--ile) {
                tags.append(";NEXT;");
            }
        }
    }
    tags.append(";END");
    conn->s_write(tags);
}

void Refreshing::send_sublist(Connection *conn) {
    ssize_t ile =  conn->get_user()->get_sublist().size();
    string user_tags = "USR_TAG;";
    if (!conn->get_user()->get_sublist().empty()) {
        for (auto *v : conn->get_user()->get_sublist()) {
            user_tags.append(v->get_tagname());
            if (--ile) {
                user_tags.append(";NEXT;");
            }
        }
    }
    user_tags.append(";END");
    conn->s_write(user_tags);
}

void Refreshing::publishing(Connection *conn) {

    for (auto *v1 : conn->get_user()->get_sublist()) {
        for (auto *v2 : Publication::get_publicationlist(v1)) {
            conn->s_write(Refreshing::pubprepare(v2));
        }
    }
}

void Refreshing::publishing(Connection *conn, Tag *t) {

    for (auto *v : Publication::get_publicationlist(t)) {
        conn->s_write(Refreshing::pubprepare(v));
    }

}

void Refreshing::publishing(Publication *p) {
    string pub = Refreshing::pubprepare(p);
    for (auto *v : Connection::get_connectionlist()) {
        if (v->get_user()->check_sub(p->get_tag())) {
            v->s_write(pub);
        }
    }
}