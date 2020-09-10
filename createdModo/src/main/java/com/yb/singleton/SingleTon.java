package com.yb.singleton;

public class SingleTon {
    private SingleTon() {
        System.out.println("init singleTon");
    }

    // 由于属性必须是静态成员变量，所以内部类必须是静态内部类
    // instance的初始化只有再获取实例时调用，内部类不会在项目启动时进行类加载
    private static class InnerSingleTon{
        private static SingleTon instance = new SingleTon();
    }

    public static SingleTon getInstance(){
        System.out.println("get instance");
        return InnerSingleTon.instance;
    }

    public static void main(String[] args) {
        SingleTon.getInstance();
    }
}
