#include <iostream>
#include "working.h"

using namespace std;

int main(int argc, char* argv[]) {
    cout << "Hello, World!" << endl;

    Working::launch(argc, argv);
    Working::operation();
    cin.get();
    Working::abolish();

    return 0;
}
