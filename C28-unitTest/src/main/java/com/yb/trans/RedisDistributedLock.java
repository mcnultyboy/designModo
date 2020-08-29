package com.yb.trans;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 类作用
 *
 * @auther yb
 * @date 2020/8/29 16:23
 */
public class RedisDistributedLock extends ReentrantLock{
    private static volatile RedisDistributedLock singletonIntance;
    private List<String> lockList = new ArrayList<>();
    private RedisDistributedLock(){}

    public static RedisDistributedLock getSingletonIntance() {
        if (singletonIntance == null){
            synchronized (RedisDistributedLock.class){
                if (singletonIntance == null){
                    singletonIntance = new RedisDistributedLock();
                }
            }
        }
        return singletonIntance;
    }

    public boolean lockTransaction(String id){
        if (lockList.contains(id)){
            return false;
        }
        lockList.add(id);
        super.lock();
        return true;
    }

    public void unlockTransaction(String id){
        lockList.remove(id);
        super.unlock();
    }
}
