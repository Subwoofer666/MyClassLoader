import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MyClassloader extends ClassLoader {
    private final Map classesHash = new HashMap();
    public final String[] classPath;


    public MyClassloader(String[] classPath) {
        this.classPath = classPath;
    }


    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class result = findClass(name);
        if (resolve)
            resolveClass(result);
        return result;
    }


    protected Class findClass(String name) throws ClassNotFoundException {
        Class result = (Class)classesHash.get(name);
        if (result != null) {
            return result;
        }

        File f= findFile(name.replace('.','/'),".class");

        if (f==null) {
            return findSystemClass(name);
        }

        try {
            byte[] classBytes= loadFile(f);
            result= defineClass(name, classBytes, 0,
                    classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(
                    "Ошибка загрузки класса " + name + ": " + e);
        }
        classesHash.put(name,result);
        return result;
    }


    private File findFile(String name, String extension) {
        File f;
        for (int k=0; k <classPath.length; k++) {
            f = new File((new File(classPath[k])).getPath()
                    + File.separatorChar
                    + name.replace('/',
                    File.separatorChar)
                    + extension);
            if (f.exists())
                return f;
        }
        return null;
    }


    public static byte[] loadFile(File file) throws IOException {
        byte[] result = new byte[(int)file.length()];
        FileInputStream f = new FileInputStream(file);
        try {
            f.read(result,0,result.length);
        } finally {
                f.close();
        }
        return result;
    }

    protected java.net.URL findResource(String name) {
        File f= findFile(name, "");
        if (f==null)
            return null;
        try {
            return f.toURL();
        } catch(java.net.MalformedURLException e) {
            return null;
        }
    }
}