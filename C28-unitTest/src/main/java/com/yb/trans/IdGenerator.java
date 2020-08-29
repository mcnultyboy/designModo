package com.yb.trans;

import java.util.Random;

/**
 * 类作用
 *
 * @auther yb
 * @date 2020/8/29 16:23
 */
public class IdGenerator {
    private static Random r = new Random();

    public static String generateTransactionId() {
        return String.valueOf(r.nextInt());
    }
}
