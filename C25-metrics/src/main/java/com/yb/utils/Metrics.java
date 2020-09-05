package com.yb.utils;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * api接口调用信息统计类
 *
 * @auther yb
 * @date 2020/9/5 15:39
 */
@Component
public class Metrics{

    public Metrics() {
        startRepeatedReport(10, TimeUnit.SECONDS);
    }

    /**key-api接口name，value-api接口调用的时间戳或完成整个调用所用的响应时间*/
    private Map<String, List<Double>> timestamps = new ConcurrentHashMap<>();
    private Map<String, List<Double>> responseTimes = new ConcurrentHashMap<>();

    /**记录api接口调用时间戳*/
    public void recordTimestamp(String apiName, Double timestamp){
        timestamps.putIfAbsent(apiName, new ArrayList<>());
        timestamps.get(apiName).add(timestamp);
    }

    /**记录api接口调用响应时间*/
    public void recordResponseTime(String apiName, Double responseTime){
        responseTimes.putIfAbsent(apiName, new ArrayList<>());
        responseTimes.get(apiName).add(responseTime);
    }

    /**统计api调用指标信息 max min 等*/
    public void startRepeatedReport(long period, TimeUnit unit){
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(()->{
            Gson gson = new Gson();
            // Map<apiName, Map<stat, value>>
            Map<String, Map<String, Double>> stats = new HashMap<>();
            // 统计响应时间的最大值和平均值
            for (Map.Entry<String, List<Double>> entry : responseTimes.entrySet()) {
                String apiName = entry.getKey();
                List<Double> respTimeList = entry.getValue();
                stats.putIfAbsent(apiName, new HashMap<>());
                Map<String, Double> statMap = stats.get(apiName);
                statMap.put("max", max(respTimeList));
                statMap.put("avg", avg(respTimeList));
                stats.put(apiName, statMap);
            }

            //统计调用api的次数
            for (Map.Entry<String, List<Double>> entry : timestamps.entrySet()) {
                String apiName = entry.getKey();
                List<Double> timestampList = entry.getValue();
                stats.putIfAbsent(apiName, new HashMap<>());
                Map<String, Double> statMap = stats.get(apiName);
                statMap.put("count", new Double(timestampList.size()));
                stats.put(apiName, statMap);
            }
            // 控制台输出
            System.out.println(gson.toJson(stats));
        }, 0,period, unit);
    }

    private Double max(List<Double> timestampList) {
        Double max = null;
        for (int i = 0; i < timestampList.size(); i++) {
            Double cur = timestampList.get(i);
            if (i == 0){
                max = cur;
                continue;
            }
            max = Math.max(max, cur);
        }
        return max;
    }

    private Double avg(List<Double> timestampList) {
        Double sum = new Double(0);
        for (Double cur : timestampList) {
            sum = Double.sum(sum, cur);
        }
        Double avg = new Double(sum.doubleValue()/timestampList.size());
        return avg;
    }


}
