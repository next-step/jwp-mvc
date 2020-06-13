package study.reflection;

public class MyObject {
    private final static String A = "A";
    public final static String B = "B";

    private String c = "C";
    String d = "D";
    protected String e = "E";
    public String f = "F";

    private MyObject() {}

    public MyObject(int num, long str) {}

    public MyObject(long str, int num) {}

    MyObject(String c1, String c2) {
        this.c = c1 + c2;
    }

    protected MyObject(int num, String e) {
        this.e = e + num;
    }

    protected MyObject(String e, int num) {
        this.e = e + num;
    }

    public MyObject(String f) {
        this.f = f;
    }

    private int sum(int a, int b) {
        return a + b;
    }

    int multiply(int a, int b) {
        return a * b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

}
