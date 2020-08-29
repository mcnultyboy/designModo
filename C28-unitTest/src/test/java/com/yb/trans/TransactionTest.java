package com.yb.trans;

import org.junit.Test;

import javax.transaction.InvalidTransactionException;

import static org.junit.Assert.*;

/**
 * 类作用
 *
 * @auther yb
 * @date 2020/8/29 16:24
 */
public class TransactionTest {
    /**
     * 1.正常情况下，交易执行成功，
     * 回填用于对账（交易与钱包的交易流水）用的 walletTransactionId，交易状态设置为 EXECUTED，函数返回 true。
     * 2.buyerId、sellerId 为 null、amount 小于 0，返回 InvalidTransactionException。
     * 3.交易已过期（createTimestamp 超过 14 天），交易状态设置为 EXPIRED，返回 false。
     * 4.交易已经执行了（status==EXECUTED），不再重复执行转钱逻辑，返回 true。
     * 5.钱包（WalletRpcService）转钱失败，交易状态设置为 FAILED，函数返回 false。
     * 6.交易正在执行着，不会被重复执行，函数直接返回 false。
     */
    /**
     * 1.正常情况下，交易执行成功，
     * 回填用于对账（交易与钱包的交易流水）用的 walletTransactionId，交易状态设置为 EXECUTED，函数返回 true。
     *
     * 通过依赖注入和mock，让单元测试不再依赖外部不可控的服务
     */
    @Test
    public void testExecute() throws InvalidTransactionException {
        String preAssignedId = "123";
        Long buyerId = 666L;
        Long sellerId = 777L;
        Long productId = 23333L;
        String orderId = "123456";
        Double amount = 100.00D;
        Transaction transaction = new Transaction(preAssignedId, buyerId, sellerId, productId, orderId, amount);
        // 使用mock的service
        WalletRpcService mockService = new WalletRpcService(){
            @Override
            public String moveMoney(String id, Long buyerId, Long sellerId, Double amount) {
                return "mockId";
            }
        };
        transaction.setWalletRpcService(mockService);
        // 使用mock的lock
        TransactionLock mockLock = new TransactionLock() {

            @Override
            public boolean lock(String id) {
                return true; // 模拟加锁直接成功
            }

            @Override
            public void unlock(String id) {
                return;
            }
        };
        transaction.setTransactionLock(mockLock);
        boolean result = transaction.execute();
        assertTrue(result);
        assertEquals(STATUS.EXECUTED, transaction.getStatus());
    }

    /**
     * 2.buyerId、sellerId 为 null、amount 小于 0，返回 InvalidTransactionException。
     */
    @Test
    public void testExecute_buyerId_sellerId_amount(){
        String preAssignedId = "123";
        Long buyerId = null;
        Long sellerId = null;
        Long productId = 23333L;
        String orderId = "123456";
        Double amount = -100.00D;
        Transaction transaction = new Transaction(preAssignedId, buyerId, sellerId, productId, orderId, amount);
        // 使用mock的service
        WalletRpcService mockService = new WalletRpcService(){
            @Override
            public String moveMoney(String id, Long buyerId, Long sellerId, Double amount) {
                return "mockId";
            }
        };
        transaction.setWalletRpcService(mockService);
        // 使用mock的lock
        TransactionLock mockLock = new TransactionLock() {

            @Override
            public boolean lock(String id) {
                return true; // 模拟加锁直接成功
            }

            @Override
            public void unlock(String id) {
                return;
            }
        };
        transaction.setTransactionLock(mockLock);
        try {
            transaction.execute();
        } catch (InvalidTransactionException e) {
            e.printStackTrace();
            assertEquals("buyerId or sellerId or amount is null", e.getMessage());
        }
    }

    /**
     * 3.交易已过期（createTimestamp 超过 14 天），交易状态设置为 EXPIRED，返回 false。
     */
    @Test
    public void testExecute_with_transactionExpired() throws InvalidTransactionException {
        String preAssignedId = "123";
        Long buyerId = 666L;
        Long sellerId = 777L;
        Long productId = 23333L;
        String orderId = "123456";
        Double amount = 100.00D;
        Transaction transaction = new Transaction(preAssignedId, buyerId, sellerId, productId, orderId, amount){
            @Override
            public boolean isExpired() {
                return true;
            }
        };
        // 使用mock的service
        WalletRpcService mockService = new WalletRpcService(){
            @Override
            public String moveMoney(String id, Long buyerId, Long sellerId, Double amount) {
                return "mockId";
            }
        };
        transaction.setWalletRpcService(mockService);
        // 使用mock的lock
        TransactionLock mockLock = new TransactionLock() {

            @Override
            public boolean lock(String id) {
                return true; // 模拟加锁直接成功
            }

            @Override
            public void unlock(String id) {
                return;
            }
        };
        transaction.setTransactionLock(mockLock);
        boolean result = transaction.execute();
        assertFalse(result);
        assertEquals(STATUS.EXPIRED, transaction.getStatus());
    }

    //4.交易已经执行了（status==EXECUTED），不再重复执行转钱逻辑，返回 true
    @Test
    public void testExecute_hasExecuted() throws InvalidTransactionException {
        String preAssignedId = "123";
        Long buyerId = 666L;
        Long sellerId = 777L;
        Long productId = 23333L;
        String orderId = "123456";
        Double amount = 100.00D;
        Transaction transaction = new Transaction(preAssignedId, buyerId, sellerId, productId, orderId, amount);
        // 使用mock的service
        WalletRpcService mockService = new WalletRpcService(){
            @Override
            public String moveMoney(String id, Long buyerId, Long sellerId, Double amount) {
                return "mockId";
            }
        };
        transaction.setWalletRpcService(mockService);
        // 使用mock的lock
        TransactionLock mockLock = new TransactionLock() {

            @Override
            public boolean lock(String id) {
                return true; // 模拟加锁直接成功
            }

            @Override
            public void unlock(String id) {
                return;
            }
        };
        transaction.setTransactionLock(mockLock);
        // 模拟交易已经执行
        transaction.setStatus(STATUS.EXECUTED);
        boolean result = transaction.execute();
        assertTrue(result);
    }

    // 5.钱包（WalletRpcService）转钱失败，交易状态设置为 FAILED，函数返回 false。
    @Test
    public void testExecute_moveMoneyFaild() throws InvalidTransactionException {
        String preAssignedId = "123";
        Long buyerId = 666L;
        Long sellerId = 777L;
        Long productId = 23333L;
        String orderId = "123456";
        Double amount = 100.00D;
        Transaction transaction = new Transaction(preAssignedId, buyerId, sellerId, productId, orderId, amount);
        // 使用mock的service
        WalletRpcService mockService = new WalletRpcService(){
            @Override
            public String moveMoney(String id, Long buyerId, Long sellerId, Double amount) {
                return null; // 转钱失败返回null
            }
        };
        transaction.setWalletRpcService(mockService);
        // 使用mock的lock
        TransactionLock mockLock = new TransactionLock() {

            @Override
            public boolean lock(String id) {
                return true; // 模拟加锁直接成功
            }

            @Override
            public void unlock(String id) {
                return;
            }
        };
        transaction.setTransactionLock(mockLock);
        boolean result = transaction.execute();
        assertFalse(result);
        assertEquals(STATUS.FAILED, transaction.getStatus());
    }


}