#include <iostream>
#include "working.h"

using namespace std;

int main(int argc, char* argv[]) {
    cout << "Hello, World!" << endl;

    //TODO wstępne tagi     OK:T
    //TODO publish          OK:T
    //TODO serializacja     -
    //TODO ...

    Working::launch(argc, argv);
    Working::operation();
    cin.get();
    Working::abolish();


    return 0;
}
