#include "decipher.h"
#include "user.h"
#include "publication.h"
#include "refreshing.h"

string Decipher::part(string tekst, unsigned int p) {
    size_t licznik = 0, alpha = 0, beta = -1;
    size_t pozycja = -1;
    while (licznik <= p) {
        pozycja = tekst.find(';', pozycja + 1);
        if (pozycja < tekst.size()) {
            alpha = beta + 1;
            beta = pozycja;
            licznik++;
        } else if (licznik == p) {
        return tekst.substr(beta + 1);
        } else {
            return "QQQEND"; //TODO
        }
    }
    return tekst.substr(alpha, beta - alpha);
}

string Decipher::next(string &tekst) {
    string ret;
    ret = tekst.substr(0, tekst.find(';'));
    tekst = tekst.substr(tekst.find(';')+1);
    return ret;
}

string Decipher::next_l(string &tekst) {
    string ret, s_ile;
    ssize_t ile = 0;
    s_ile = next(tekst);
    try {
        ile = stoi(s_ile);
    } catch (...) {
        cout << "błąd w długości pola" << endl;
        return nullptr;
    }
    ret = tekst.substr(0, ile);

    if (tekst[ile] != ';') {
        cout << "błąd w długości pola" << endl;
        return nullptr;
    }

    tekst = tekst.substr(ile+1);

    return ret;
}

bool Decipher::last(string &tekst) {
    return tekst == "END";
}


void Decipher::a_join(string analiza, Connection *conn) {
//    if (tekst.substr(5,7) == "old;END" && User::get_userlist().empty()) {
//        conn->s_write("ERR_JOIN;no_old;END");
//    } else if (tekst.substr(5,7) == "old;END" && !User::get_userlist().empty()) {
//        conn->s_write("ACK_JOIN;old;END");
//    } else if (tekst.substr(5,7) == "new;END") {
//        conn->s_write("ACK_JOIN;new;END");
//    } else {
//        conn->s_write("ERR_JOIN;" + tekst.substr(5,tekst.size()-6) + ";END");
//    }
    if (!last(analiza)) {
        conn->s_write("ERR_LOG;no END;END");
    } else {
        conn->s_write("ACK_JOIN;END");
    }
};

//void Decipher::a_join_new(string tekst, Connection *conn) {
//    string nick = Decipher::part(tekst, 1);
//    string password = Decipher::part(tekst, 2);
//    if (nick == "QQQEND" || password == "QQQEND") {
//        conn->s_write("ERR_JOIN;fields_problem;END"); //TODO
//        return;
//    }
//    bool istnieje = false;
//    for (auto v : User::get_userlist()) {
//        if (v->get_nick() == nick) {
//            istnieje = true;
//            break;
//        }
//    }
//    if (istnieje) {
//        conn->s_write("ERR_JOIN;name (" + nick +") busy;END");
//    } else {
//        User * user = new User(nick, password);
//        conn->assign(user); //TODO
//        conn->s_write("ACK_JOIN_NEW;END");
//
//        Refreshing::send_taglist(conn);
//        Refreshing::send_sublist(conn);
//        Refreshing::publishing(conn);
//    }
//};
//
//void Decipher::a_join_old(string tekst, Connection *conn) {
//    string nick = part(tekst, 1);
//    string password = part(tekst, 2);
//    if (nick == "QQQEND" || password == "QQQEND") {
//        conn->s_write("ERR_JOIN;fields_problem;END"); //TODO
//        return;
//    }
////    bool istnieje = false;
////    User * user;
////    for (auto v : User::get_userlist()) {
////        if (v->get_nick() == nick) {
////            istnieje = true;
////            user = v;
////            break;
////        }
////    }
//    User * user = User::get_user(nick);
////    if (!istnieje) {
//    if (user == nullptr) {
//        conn->s_write("ERR_JOIN;user (" + nick + ") does not exist;END");
//    } else if (user->check_password(password)){
//        conn->assign(user); //TODO
//        conn->s_write("ACK_JOIN_OLD;END");
//
//        Refreshing::send_taglist(conn);
//        Refreshing::send_sublist(conn);
//        Refreshing::publishing(conn);
//    } else {
//        conn->s_write("ERR_JOIN;wrong password;END");
//    }
//};

void Decipher::a_log(string analiza, Connection *conn) {
    string nick = next_l(analiza);
    cout << nick << endl;
    string password = next_l(analiza);
    cout << password << endl;
    cout << analiza << endl;
    if (!last(analiza)) {
        conn->s_write("ERR_LOG;no END;END");
    } else {
        User *user = User::get_user(nick);
        if (user != nullptr) {
            if (user->check_password(password)) {
                conn->assign(user);
                conn->s_write("ACK_LOG;END");

                Refreshing::send_taglist(conn);
                Refreshing::send_sublist(conn);
                Refreshing::publishing(conn);
            } else {
                conn->s_write("ERR_LOG;wrong password;END");
            }
        } else {
            user = new User(nick, password);
            conn->assign(user);
            conn->s_write("ACK_LOG;END");

            Refreshing::send_taglist(conn);
            Refreshing::send_sublist(conn);
            Refreshing::publishing(conn);
        }
    }
}

void Decipher::a_sub_add(string analiza, Connection *conn) {
    if (conn->get_user() == nullptr) {
        conn->s_write("ERR_SUB_ADD;user not logged in;END");
        return;
    }
    string tag = next(analiza);
    if (!last(analiza)) {
        conn->s_write("ERR_LOG;no END;END");
    } else {

        Tag *t = Tag::get_tag(tag);
        if (t == nullptr) {
            conn->s_write("ERR_SUB;tag (" + tag + ") does not exist;END");
        } else {
            conn->get_user()->add_sub(t);
            conn->s_write("ACK_SUB;" + tag + ";END");
            Refreshing::send_sublist(conn);
            Refreshing::publishing(conn, t);
        }
    }
};

void Decipher::a_sub_del(string analiza, Connection *conn) {
    if (conn->get_user() == nullptr) {
        conn->s_write("ERR_SUB_DEL;user not logged in;END");
        return;
    }
    string tag = next(analiza);
    if (!last(analiza)) {
        conn->s_write("ERR_LOG;no END;END");
    } else {
        Tag *t = Tag::get_tag(tag);
        if (t == nullptr) {
            conn->s_write("ERR_SUB;tag (" + tag + ") does not exist;END");
        } else {
            conn->get_user()->del_sub(t);
            conn->s_write("ACK_SUB;" + tag + ";END");
            Refreshing::send_sublist(conn);
        }
    }
};

//void Decipher::a_sub(string tekst, Connection *conn) {
//    if (conn->get_user() == nullptr) {
//        conn->s_write("ERR_SUB;user not logged in;END");
//        return;
//    }
//    string way = part(tekst, 1);
//    string tag = part(tekst, 2);
//    if (way == "QQQEND" || tag == "QQQEND" || !(way == "T" || way == "F")) {
//        conn->s_write("ERR_SUB;fields_problem;END"); //TODO
//        return;
//    }
////    bool istnieje = false;
////    Tag * t;
////    if (!Tag::get_taglist().empty()) {
////        for (auto v : Tag::get_taglist()) {
////            if (v->get_tagname() == tag) {
////                istnieje = true;
////                t = v;
////                break;
////            }
////        }
////    }
//    Tag * t = Tag::get_tag(tag);
//    if (t == nullptr) {
////    if (!istnieje) {
//        conn->s_write("ERR_SUB;tag (" + tag + ") does not exist;END");
//    } else if (way == "T") {
//        conn->get_user()->add_sub(t);
//        conn->s_write("ACK_SUB;T;" + tag + ";END");
//        Refreshing::send_sublist(conn);
//        Refreshing::publishing(conn, t);
//    } else if (way == "F") {
//        conn->get_user()->del_sub(t);
//        conn->s_write("ACK_SUB;F;" + tag + ";END");
//        Refreshing::send_sublist(conn);
//    } else {
//        conn->s_write("ERR_SUB;?????;END");
//    }
//};

void Decipher::a_send(string analiza, Connection *conn) {
    if (conn->get_user() == nullptr) {
        conn->s_write("ERR_SEND;user not logged in;END");
        return;
    }

    string tag = next(analiza);
    string title = next_l(analiza);
    string content = next_l(analiza);
    if (!last(analiza)) {
        conn->s_write("ERR_LOG;no END;END");
    } else {
        Tag *t = Tag::get_tag(tag);
        if (t == nullptr) {
            conn->s_write("ERR_SEND;tag (" + tag + ") does not exist;END");
        } else {
            Publication *p = new Publication(t, title, conn->get_user(), content);
            conn->s_write("ACK_SEND;END");
            Refreshing::publishing(p);
        }
    }
};




//void Decipher::a_send_pub(string tekst, Connection *conn) {
//    if (conn->get_user() == nullptr) {
//        conn->s_write("ERR_SEND_PUB;user not logged in;END");
//        return;
//    }
//    string tag = Decipher::part(tekst, 1);
//    string title = Decipher::part(tekst, 2);
//    string content = Decipher::part(tekst, 3);
//    if (tag == "QQQEND" || title == "QQQEND" || content == "QQQEND") {
//        conn->s_write("ERR_SEND_PUB;fields_problem;END"); //TODO
//        return;
//    }
////    bool istnieje = false;
////    Tag * t;
////    if (!Tag::get_taglist().empty()) {
////        for (auto v : Tag::get_taglist()) {
////            if (v->get_tagname() == tag) {
////                istnieje = true;
////                t = v;
////                break;
////            }
////        }
////    }
//    Tag * t = Tag::get_tag(tag);
////    if (!istnieje) {
//    if (t == nullptr) {
//        conn->s_write("ERR_SEND_PUB;tag (" + tag + ") does not exist;END");
//        //TODO tworzenie nowego?
//    } else {
//        Publication * p = new Publication(t, title, conn->get_user(), content); //TODO if doesn;t work
//        conn->s_write("ACK_SEND_PUB;END");
//        Refreshing::publishing(p); //TODO if doesn;t work
//
//    }
//};

void Decipher::a_term(string analiza, Connection *conn) {

    if (!last(analiza)) {
        conn->s_write("ERR_TERM;no END;END");
    } else {
        conn->s_write("ACK_TERM;END");
        conn->disable();
    }
};

void Decipher::study(string komunikat, Connection * connection) {

    string start = next(komunikat);

    if (start == "JOIN") {
        a_join(komunikat, connection);
    } else if (start == "LOG") {
        a_log(komunikat, connection);
//    } else if (start == "JOIN_NEW") {
//        a_join_new(komunikat, connection);
//    } else if (start == "JOIN_OLD") {
//        a_join_old(komunikat, connection);
    } else if (start == "SUB_ADD") {
        a_sub_add(komunikat, connection);
    } else if (start == "SUB_DEL") {
        a_sub_del(komunikat, connection);
    } else if (start == "SEND") {
        a_send(komunikat, connection);
    } else if (start == "TERM") {
        a_term(komunikat, connection);
    } else if (start == "QQQ") {
        connection->s_write("ERROR;0"); //TODO
    } else {
        connection->s_write("ERROR;BAD_TASK;END"); //TODO
    }
};