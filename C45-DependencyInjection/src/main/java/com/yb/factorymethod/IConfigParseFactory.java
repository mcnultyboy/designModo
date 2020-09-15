package com.yb.factorymethod;

import com.yb.parser.IConfigParser;

// 当每个类依赖的对象多，或者创建很复杂的时候，需要为每个对象准备一个factory
public interface IConfigParseFactory {
    IConfigParser createConfigParser();
}
