package com.yb.trans;

/**
 * DistributeLock的包装类
 *
 * @auther yb
 * @date 2020/8/29 17:14
 */
public class TransactionLock {
    // 对distributeLock类进行包装之后，方便测试
    public boolean lock(String id){
        RedisDistributedLock singletonIntance = RedisDistributedLock.getSingletonIntance();
        return singletonIntance.lockTransaction(id);
    }

    public void unlock(String id){
        RedisDistributedLock singletonIntance = RedisDistributedLock.getSingletonIntance();
        singletonIntance.unlockTransaction(id);
    }
}
