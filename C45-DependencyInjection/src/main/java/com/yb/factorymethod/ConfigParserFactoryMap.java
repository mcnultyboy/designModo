package com.yb.factorymethod;


import com.yb.parser.IConfigParser;

import java.util.HashMap;
import java.util.Map;

// 工厂的工厂，用来获取对应的工厂并创建对象
public class ConfigParserFactoryMap {
    private static Map<String, IConfigParseFactory> map = new HashMap();

    static {
        map.put("json", new JsonConfigParseFactory());
        map.put("xml", new XmlConfigParseFactory());
        map.put("yaml", new YamlConfigParseFactory());
    }

    public static IConfigParser createConfigParser(String configFormat){
        IConfigParseFactory factory = map.get(configFormat);
        IConfigParser configParser = factory.createConfigParser();
        return configParser;
    }
}
