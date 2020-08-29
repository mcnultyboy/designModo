package com.yb.trans;

/**
 * 类作用
 *
 * @auther yb
 * @date 2020/8/29 16:24
 */
public class WalletRpcService {
    public WalletRpcService() {
    }

    public String moveMoney(String id, Long buyerId, Long sellerId, Double amount) {
        System.out.println("moveMoney");
        return "transfer ok";
    }
}
