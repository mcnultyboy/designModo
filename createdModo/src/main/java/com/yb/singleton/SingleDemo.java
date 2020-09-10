package com.yb.singleton;


/**
 * 这种方式在instance不为null的时候要比双检锁减少一次对主存的访问，性能更好。
 * 传统的双检查锁，在1 2 两处都用到了instacne，由于volatile会强制到主存中获取数据，所以要访问2次主存。
 * 这种方式赋值给
 * */
public class SingleDemo {
    private SingleDemo(){}

    private static volatile SingleDemo instance = null;

    public static SingleDemo getInstance(){
        SingleDemo temp = instance; // 1
        if (temp == null){
            synchronized (SingleDemo.class){
                temp = instance;
                if (temp == null){
                    instance = new SingleDemo();
                    temp = instance;
                }
            }
        }
        return  temp; // 2
    }

    public static void main(String[] args) {
        IdGenarator.INSTANCE.getId();
    }

}
