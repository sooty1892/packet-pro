public class Hello {
    public native void sayHi(String who, int times);

    static { System.loadLibrary("Hello"); }

    public static void main(String[] args) {
        Hello hello = new Hello();
        
        hello.sayHi(args[0], Integer.parseInt(args[1]));
    }
}
