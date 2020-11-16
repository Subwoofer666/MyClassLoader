
public class Main {

    public static void main(String[] argv) throws Exception {
        ClassLoader loader = new MyClassloader(new String[]{"."});
        Class clazz = Class.forName("Test", true, loader);
        Object object = clazz.newInstance();
        System.out.println(object);
        }
    }

