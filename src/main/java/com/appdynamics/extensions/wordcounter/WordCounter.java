package com.appdynamics.extensions.wordcounter;

import com.appdynamics.extensions.ABaseMonitor;
import com.appdynamics.extensions.MetricWriteHelper;
import com.appdynamics.extensions.TasksExecutionServiceProvider;
import com.appdynamics.extensions.wordcounter.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


public class WordCounter extends ABaseMonitor {

    private final static String monitorName = "WordCount";
    MetricWriteHelper metricWriteHelper;



    public static String getMetricPrefix() {
        return Constants.DEFAULT_METRIC_PREFIX;
    }

    private static final Logger logger = LoggerFactory.getLogger(WordCounter.class);

    @Override
    protected void doRun(TasksExecutionServiceProvider tasksExecutionServiceProvider) {

        List<Map<String, String>> fileList = (List<Map<String,String>>)configuration.getConfigYml().get("files");
        logger.debug("fileList : {}", fileList.size());
        for(Map<String, String> file : fileList){
             // get each file path
            String filePath = file.get("filePath").toString();
            String fileName = file.get("fileName").toString();
            WordCounterTask wordCounterTask = new WordCounterTask(filePath,fileName, tasksExecutionServiceProvider, tasksExecutionServiceProvider.getMetricWriteHelper());
            tasksExecutionServiceProvider.submit(fileName, wordCounterTask);
        }


    }


    @Override
    public String getMonitorName() {
        return monitorName;
    }

    @Override
    protected int getTaskCount() {
        List<Map<String, String>> fileList = (List<Map<String,String>>)configuration.getConfigYml().get("files");
        logger.debug("fileList : {}", fileList.size());
        return fileList.size();
    }

    @Override
    protected String getDefaultMetricPrefix() {
        return Constants.DEFAULT_METRIC_PREFIX;
    }



}
