package com.appdynamics.extensions.wordcounter;

import com.appdynamics.extensions.AMonitorTaskRunnable;
import com.appdynamics.extensions.MetricWriteHelper;
import com.appdynamics.extensions.TasksExecutionServiceProvider;
import com.appdynamics.extensions.conf.MonitorConfiguration;
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


public class WordCounterTask implements AMonitorTaskRunnable {

    private static final Logger logger = LoggerFactory.getLogger(WordCounter.class);
    private MonitorConfiguration configuration;
    private MetricWriteHelper metricWriteHelper;
    private TasksExecutionServiceProvider tasksExecutionServiceProvider;
    private String filePath;
    private String fileName;
    Map resultMap = new HashMap<String, Integer>();
    String line = null;
    List<Metric> finalMetricList;


    public WordCounterTask(String filePath, String fileName, TasksExecutionServiceProvider tasksExecutionServiceProvider, MetricWriteHelper metricWriteHelper) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.tasksExecutionServiceProvider = tasksExecutionServiceProvider;
        this.metricWriteHelper = metricWriteHelper;
    }

    @Override
    public void run() {

        metricWriteHelper.transformAndPrintMetrics(populateAndPrintStats(filePath, fileName));
    }

    private List<Metric> populateAndPrintStats(String filePath, String fileName){
        try{
            FileReader fileReader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fileReader);
            while((line = br.readLine())!=null){
                String[] array = (line.split("\\s+"));
                for(String str : array){
                    if(resultMap.containsKey(str)){
                        logger.debug("contains already in:" + fileName);
                        resultMap.put(str,  ((int)resultMap.get(str)) + 1 );
                    }
                    else {
                        logger.debug("doesn't contain in:" + fileName);
                        resultMap.put(str, 1);
                    }
                }
            }
        }catch(FileNotFoundException fe ){
            System.out.print(fe);
        } catch (IOException ioe){
            System.out.print(ioe);

        }

       return combineMetrics(resultMap);
    }

    private List<Metric>  combineMetrics(Map<String,Integer> resultMap){

        finalMetricList = new ArrayList<Metric>();

        for(Map.Entry entry : resultMap.entrySet()){
            String metricPath = Constants.DEFAULT_METRIC_PREFIX + fileName ;
            Metric metric = new Metric(entry.getKey().toString(), entry.getValue().toString(), metricPath + "|" + entry.getKey().toString() );
            finalMetricList.add(metric);

        }

        return finalMetricList;

    }

     @Override
    public void onTaskComplete() {

    }
}
