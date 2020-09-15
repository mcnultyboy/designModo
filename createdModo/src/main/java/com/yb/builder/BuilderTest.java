package com.yb.builder;

public class BuilderTest {
    public static void main(String[] args) {
        ResourcePoolConfig source1 = new ResourcePoolConfig.Builder()
                .setMaxIdle(5)
                .setMinIdle(2)
                .setName("source1")
                .build();
    }
}
