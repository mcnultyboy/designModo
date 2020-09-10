package com.yb.singleton;

public class ClassLoadTest {

    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = ClassLoadTest.class.getClassLoader();
        System.out.println(classLoader);
        MyClassLoader1 myClassLoader1 = new MyClassLoader1("E:/Person.class");
        MyClassLoader2 myClassLoader2 = new MyClassLoader2("E:/Person.class");
        Class<?> var1 = myClassLoader1.findClass("com.yb.singleton.Person");
        Class<?> var2 = myClassLoader2.findClass("com.yb.singleton.Person");
        System.out.println(var1 == var2);
        System.out.println(var1.getClassLoader());
        System.out.println(var2.getClassLoader());
    }
}
