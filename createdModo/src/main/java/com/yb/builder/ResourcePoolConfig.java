package com.yb.builder;

public class ResourcePoolConfig {
    private String name; private int maxTotal; private int maxIdle; private int minIdle;

    // 私有构造器，只能使用builder创建config对象
    private ResourcePoolConfig(Builder builder){
        this.name = builder.name;
        this.maxTotal = builder.maxTotal;
        this.maxIdle = builder.maxIdle;
        this.minIdle = builder.minIdle;
    }

    public static class Builder{
        private String name; private int maxTotal; private int maxIdle; private int minIdle;
        private static final int MAX_TOTAL_DEFAULT = 10;
        private static final int MAX_IDLE_DEFAULT = 9;
        private static final int MIN_IDLE_DEFAULT = 1;

        public ResourcePoolConfig build(){
            // 进行参数检查以及默认值填充
            if (this.maxTotal == 0){
                this.maxTotal = MAX_TOTAL_DEFAULT;
            }
            if (this.maxIdle == 0){
                this.maxIdle = MAX_IDLE_DEFAULT;
            }
            if (this.minIdle == 0){
                this.minIdle = MIN_IDLE_DEFAULT;
            }
            if (minIdle > maxIdle){
                throw new RuntimeException("cannot equal");
            }
            return new ResourcePoolConfig(this);
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
            return this;
        }

        public Builder setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
            return this;
        }

        public Builder setMinIdle(int minIdle) {
            this.minIdle = minIdle;
            return this;
        }
    }
}
