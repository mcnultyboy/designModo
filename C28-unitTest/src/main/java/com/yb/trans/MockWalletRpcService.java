package com.yb.trans;

public class MockWalletRpcService extends WalletRpcService {
    @Override
    public String moveMoney(String id, Long buyerId, Long sellerId, Double amount) {
        return "mockId";
    }
}
