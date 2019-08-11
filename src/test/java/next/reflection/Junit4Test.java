package next.reflection;

public class Junit4Test {
    @MyTest
    public void one() throws Exception {
        System.out.println("Running Test1");
    }

    @MyTest
    public void two() throws Exception {
        System.out.println("Running Test2");
    }

    public void testThree() throws Exception {
        System.out.println("Running Test3");
    }

    @MyTest
    private void four() {
        System.out.println("private Test4");
    }

    @MyTest
    protected void five() {
        System.out.println("protected Test5");
    }

    @MyTest
    private static void six() {
        System.out.println("private static Test6");
    }

}
