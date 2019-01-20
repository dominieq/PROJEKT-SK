#include <unistd.h>
#include "refreshing.h"

string Refreshing::pubprepare(Publication *p) {
    string pub;
    pub.append("PUB;");
    pub.append(p->get_tag()->get_tagname());
    pub.append(";");
    pub.append(p->get_title());
    pub.append(";");
    pub.append(p->get_author()->get_nick());
    pub.append(";");
    pub.append(p->get_date_s());
    pub.append(";");
    pub.append(p->get_content());
    pub.append(";END");
    return pub;
}


void Refreshing::send_taglist(Connection *conn) {
    string tags = "TAG;";
    if (!Tag::get_taglist().empty()) {
        for (auto *v : Tag::get_taglist()) {
            tags.append(v->get_tagname());
            tags.append(";NEXT;");
        }
    }
    tags.append("END");
    conn->s_write(tags);
}

void Refreshing::send_sublist(Connection *conn) {
    string user_tags = "USR_TAG;";
    if (!conn->get_user()->get_sublist().empty()) {
        for (auto *v : conn->get_user()->get_sublist()) {
            user_tags.append(v->get_tagname());
            user_tags.append(";NEXT;");
        }
    }
    user_tags.append("END");
    conn->s_write(user_tags);
}

void Refreshing::publishing(Connection *conn) {

    for (auto *v1 : conn->get_user()->get_sublist()) {
        for (auto *v2 : Publication::get_publicationlist(v1)) {
            conn->s_write(Refreshing::pubprepare(v2));
        }
    }
}



//string user_tags = "USR_TAG;";
//if (!user->get_sublist().empty()) {
//for(auto v : user->get_sublist()) {
//user_tags.append(v->get_tagname());
//user_tags.append(";NEXT;");
//}
//}
//user_tags.append("END");
//conn->s_write(user_tags);
//
////TODO ALL
//string tags = "TAG;";
//if (!Tag::get_taglist().empty()) {
//for(auto v : Tag::get_taglist()) {
//tags.append(v->get_tagname());
//tags.append(";NEXT;");
//}
//}
//tags.append("END");
//conn->s_write(tags);
//
//publishing(user, conn);

//--------------------------------//
//void Refreshing::publishing(Publication *p) {
//    string pub = Refreshing::pubprepare(p);
//    for (auto *v : Connection::get_connectionlist()) {
//        if (v->get_user()->check_sub(p->get_tag())) {
//            v->s_write(pub);
//        }
//    }
//}
//
//void Refreshing::publishing(Tag *t, Connection *conn) {
//    for (auto *v : Publication::get_publicationlist(t)) {
//        conn->s_write(Refreshing::pubprepare(v));
//    }
//}
//
//void Refreshing::publishing(User *u, Connection *conn) {
//    for (auto *v1 : u->get_sublist()) {
//        for (auto *v2 : Publication::get_publicationlist(v1)) {
//            conn->s_write(Refreshing::pubprepare(v2));
//        }
//    }
//}



//--------------------------------//

void Refreshing::refreshing() {
    cout << "sleeep0" << endl;
    while (true) {
        cout << "sleeep1" << endl;
        sleep(5);

        cout << "sleeep2" << endl;

        for (auto *v : Connection::get_connectionlist()) {
            if (v->get_user() != nullptr) {
                send_taglist(v);
                send_sublist(v);
                publishing(v);
            }
        }
        cout << "sleeep3" << endl;
    }
}