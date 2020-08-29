package com.yb.trans;


import javax.transaction.InvalidTransactionException;

/**
 * 类作用
 *
 * @auther yb
 * @date 2020/8/29 16:23
 */
public class Transaction {
    private String id;
    private Long buyerId;
    private Long sellerId;
    private Long productId;
    private String orderId;
    private Long createTimestamp;
    private Double amount;
    private STATUS status;
    private String walletTransactionId;

    // 1.将new 更改为 依赖注入的方式，方便测试，也是面向接口编程的思想
    private WalletRpcService walletRpcService;

    public void setWalletRpcService(WalletRpcService walletRpcService) {
        this.walletRpcService = walletRpcService;
    }

    // 2.将 RedisDistributeLock 的引用，更改为包装类，并使用依赖注入的方式实现
    // 如果 RedisDistributeLock是自己项目的类则可以直接修改为依赖注入并mock的方式。如果是第三方，则需要使用包装类，然后依赖注入并mock。
    private TransactionLock transactionLock;

    public void setTransactionLock(TransactionLock transactionLock) {
        this.transactionLock = transactionLock;
    }

    // ...get() methods...

    public Transaction(String preAssignedId, Long buyerId, Long sellerId, Long productId, String orderId, Double amount) {
        if (preAssignedId != null && !preAssignedId.isEmpty()) {
            this.id = preAssignedId;
        } else {
            this.id = IdGenerator.generateTransactionId();
        }
        if (!this.id.startsWith("t_")) {
            this.id = "t_" + preAssignedId;
        }
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.productId = productId;
        this.orderId = orderId;
        this.status = STATUS.TO_BE_EXECUTD;
        this.createTimestamp = System.currentTimeMillis();
        this.amount = amount;
    }

    /**
     * 方法作用
     *
      * @param
     * @return boolean
     * @auther yb
     * date 2020/8/29 16:20
     */
    public boolean execute() throws InvalidTransactionException {
        if (buyerId == null || sellerId == null || amount < 0.0) {
            throw new InvalidTransactionException("buyerId or sellerId or amount is null");
        }
        if (status == STATUS.EXECUTED) return true;
        boolean isLocked = false;
        try {
//            isLocked = RedisDistributedLock.getSingletonIntance().lockTransaction(id); 2.修改为包装类的形式
            isLocked = transactionLock.lock(id);
            if (!isLocked) {
                return false; // 锁定未成功，返回false，job兜底执行
            }
            if (status == STATUS.EXECUTED) return true; // double check
            // 将未决行为重新封装为函数
//            if (executionInvokedTimestamp - createTimestamp > 14*24*60*60*1000) {
            if (isExpired()){
                this.status = STATUS.EXPIRED;
                return false;
            }
//            WalletRpcService walletRpcService = new WalletRpcService(); 1.更改为依赖注入的方式
            String walletTransactionId = walletRpcService.moveMoney(id, buyerId, sellerId, amount);
            if (walletTransactionId != null) {
                this.walletTransactionId = walletTransactionId;
                this.status = STATUS.EXECUTED;
                return true;
            } else {
                this.status = STATUS.FAILED;
                return false;
            }
        } finally {
            if (isLocked) {
//                RedisDistributedLock.getSingletonIntance().unlockTransaction(id);
                transactionLock.unlock(id);
            }
        }
    }

    public boolean isExpired() {
        long executionInvokedTimestamp = System.currentTimeMillis();
        return (executionInvokedTimestamp - createTimestamp > 14*24*60*60*1000);
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}
