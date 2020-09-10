package com.yb.singleton;

import java.math.BigDecimal;

public class BigdecimalDemo {
    public static void main(String[] args) {
        BigDecimal sum = new BigDecimal(100);
        BigDecimal v20 = new BigDecimal(20);
        BigDecimal v40 = new BigDecimal(40);
        System.out.println(sum.add(v20).intValue());
        System.out.println("sum=="+sum.intValue());
        System.out.println(sum.add(v40).intValue());
        System.out.println("sum=="+sum.intValue());
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.append("haha"));
        System.out.println(sb.append("1111"));
        System.out.println(sb);
        System.out.println(sb.toString());
        int i = 0;
        System.out.println(i++);


        boolean f = (1 > 2)  && (++i < 3);
        System.out.println(i);
    }
}
