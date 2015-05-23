javac Main.java
gcc -dynamiclib -o libplaying.jnilib -I/System/Library/Frameworks/JavaVM.framework/Headers Main.c -framework JavaVM
java -Djava.library.path="." Main
