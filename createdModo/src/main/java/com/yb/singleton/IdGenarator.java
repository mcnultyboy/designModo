package com.yb.singleton;

import java.util.concurrent.atomic.AtomicInteger;


/**枚举特性
 * 默认构造器私有
 * 每个实例都是 private static final Instance ins = new Instance(param...)
 * 为了方便访问给每个实例都添加了 getIns的方法，但是没有添加setIns方法，防止更改
 * 
 * */
public enum IdGenarator {
    INSTANCE;
    private AtomicInteger id = new AtomicInteger(0);

    public int getId(){
        return id.incrementAndGet();
    }
}
