package com.appdynamics.extensions.wordcounter;

import com.appdynamics.extensions.metrics.Metric;
import com.appdynamics.extensions.wordcounter.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 class WordCounterHandler {

    private static final Logger logger = LoggerFactory.getLogger(WordCounterHandler.class);

     List<Metric> populateAndPrintStats(String filePath, String fileName){
        String line;
        Map<String, Integer> resultMap = new HashMap<>();
        try{
            FileReader fileReader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fileReader);
            while((line = br.readLine())!=null){
                String[] array = (line.split("\\s+"));
                for(String str : array){
                    if(resultMap.containsKey(str))
                        resultMap.put(str,  resultMap.get(str) + 1 );
                    else
                        resultMap.put(str, 1);
                }
            }
        }catch(FileNotFoundException fe ){
            logger.debug(fileName+"is not found");
        } catch (IOException ioe){
            logger.debug("Unexpected error occurred while executing word count monitor", ioe);
        }
        return combineMetrics(resultMap, fileName);
    }

     private List<Metric>  combineMetrics(Map<String,Integer> resultMap, String fileName){
        List<Metric> finalMetricList = new ArrayList<>();
        for(Map.Entry entry : resultMap.entrySet()){
            String metricPath = Constants.DEFAULT_METRIC_PREFIX + fileName ;
            Metric metric = new Metric(entry.getKey().toString(), entry.getValue().toString(), metricPath + Constants.METRIC_SEPARATOR + entry.getKey().toString() );
            finalMetricList.add(metric);
        }
        return finalMetricList;
    }

}
