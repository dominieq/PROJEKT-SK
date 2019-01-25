//TODO odpal: nc -l 1234
// to będzie symulowało twój serwer, póki nie ogarnę jego parsera
// może się przydać flaga: set(CMAKE_CXX_FLAGS -pthread)


#include <pthread.h>        //wątki
//#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

//#include <iostream>
//using namespace std;

#define BUFSIZE 1024

//struktura zawierająca dane, które zostaną przekazane do wątku
struct thread_data_t {
    int connection_socket_descriptor;
};

//wskaźnik na funkcję opisującą zachowanie wątku
void *ThreadBehavior(void *t_data) {
    struct thread_data_t *th_data = (struct thread_data_t *) t_data;

    int odp;
    char bufor[BUFSIZE];

    while (1) {
        if ((odp = read(th_data->connection_socket_descriptor, bufor, BUFSIZE)) > 0) {
            write(1, bufor, odp);       //TODO tutaj masz wypisanie otrzymanych od serwera danych
                                        // trzeba wywołać parser i resztę na tych danych

        }
    }


    pthread_exit(NULL);
}


//funkcja obsługująca połączenie z serwerem
void handleConnection(int connection_socket_descriptor) {
    //wynik funkcji tworzącej wątek
    int create_result = 0;

    //uchwyt na wątek
    pthread_t thread1;

    //dane, które zostaną przekazane do wątku
    struct thread_data_t t_data;

    t_data.connection_socket_descriptor = connection_socket_descriptor;


    //utworzenie wątki
    create_result = pthread_create(&thread1, NULL, ThreadBehavior, (void *) &t_data);
    if (create_result) {
        printf("Błąd przy próbie utworzenia wątku, kod błędu: %d\n", create_result);
        exit(-1);
    }

    //TODO wysyłanie do serwera danych, funkcja write
    // example, znak '$' kończy połączenie od strony klienta
    char bufor[BUFSIZE];
    int n;      //ilość danych w buforze

    while (1) {
        fgets(bufor, BUFSIZE, stdin);
        n = strlen(bufor);
        if (n == 2 && bufor[0] == '$') {
            break;
        }
        write(connection_socket_descriptor, bufor, n);
    }
}


int main(int argc, char *argv[]) {
    int connection_socket_descriptor;
    int connect_result;
    struct sockaddr_in server_address;
    struct hostent *server_host_entity;

    //dwa argumenty, adres serwera i port, najlepiej na razie dodać localhost i 1234
    if (argc != 3) {
        fprintf(stderr, "Sposób użycia: %s server_name port_number\n", argv[0]);
        exit(1);
    }

    server_host_entity = gethostbyname(argv[1]);
    if (!server_host_entity) {
        fprintf(stderr, "%s: Nie można uzyskać adresu IP serwera.\n", argv[0]);
        exit(1);
    }

    //stworzenie gniazda
    connection_socket_descriptor = socket(PF_INET, SOCK_STREAM, 0);
    if (connection_socket_descriptor < 0) {
        fprintf(stderr, "%s: Błąd przy probie utworzenia gniazda.\n", argv[0]);
        exit(1);
    }

    memset(&server_address, 0, sizeof(struct sockaddr));
    server_address.sin_family = AF_INET;
    memcpy(&server_address.sin_addr.s_addr, server_host_entity->h_addr, server_host_entity->h_length);
    server_address.sin_port = htons(atoi(argv[2]));

    //połączenie
    connect_result = connect(connection_socket_descriptor, (struct sockaddr *) &server_address,
                             sizeof(struct sockaddr));
    if (connect_result < 0) {
        fprintf(stderr, "%s: Błąd przy próbie połączenia z serwerem (%s:%i).\n", argv[0], argv[1], atoi(argv[2]));
        exit(1);
    }

    handleConnection(connection_socket_descriptor);

    close(connection_socket_descriptor);
    return 0;

}