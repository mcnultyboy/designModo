package com.yb.simplefactory;

import com.yb.parser.IConfigParser;
import com.yb.parser.JsonRuleConfigParser;
import com.yb.parser.XmlRuleConfigParser;
import com.yb.parser.YamlRuleConfigParser;

import java.util.HashMap;
import java.util.Map;

// 简单工厂模式。每个对象的创建逻辑比较简单，并且if else不是很多，可以接受适当的修改
public class ConfigParseFactory {
    // 方法1，将对象在if else中创建
    public IConfigParser getConfigParser(String configFormat){
        if ("json"==configFormat){
            return new JsonRuleConfigParser();
        }else if ("xml"==configFormat){
            return new XmlRuleConfigParser();
        }else if ("yaml"==configFormat){
            return new YamlRuleConfigParser();
        }else {
            //throw exception
        }
        return null;
    }

    private static Map<String, IConfigParser> parsers = new HashMap();
    static{
        parsers.put("json", new JsonRuleConfigParser());
        parsers.put("xml", new XmlRuleConfigParser());
        parsers.put("yaml", new YamlRuleConfigParser());
    }

    // 方法2，将对象缓存在map中，避免每次获取都创建
    public static IConfigParser getConfigParser2(String configFormat){
        return parsers.get(configFormat);
    }


}
