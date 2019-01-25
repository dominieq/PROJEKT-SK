#include "decipher.h"
#include "user.h"
#include "publication.h"
#include "refreshing.h"

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
    if (!last(analiza)) {
        conn->s_write("ERR_LOG;no END;END");
    } else {
        conn->s_write("ACK_JOIN;END");
    }
};

void Decipher::a_log(string analiza, Connection *conn) {
    string nick = next_l(analiza);
    string password = next_l(analiza);
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
        conn->s_write("ERR_SUB;user not logged in;END");
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
//            Refreshing::publishing(conn, t);
            Refreshing::publishing(conn);
        }
    }
};

void Decipher::a_sub_del(string analiza, Connection *conn) {
    if (conn->get_user() == nullptr) {
        conn->s_write("ERR_SUB;user not logged in;END");
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
            Refreshing::publishing(conn);
        }
    }
};

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
    } else if (start == "SUB_ADD") {
        a_sub_add(komunikat, connection);
    } else if (start == "SUB_DEL") {
        a_sub_del(komunikat, connection);
    } else if (start == "SEND") {
        a_send(komunikat, connection);
    } else if (start == "TERM") {
        a_term(komunikat, connection);
    } else {
        connection->s_write("ERROR;BAD_TASK;END"); //TODO testowanie
        connection->disable();
    }
};