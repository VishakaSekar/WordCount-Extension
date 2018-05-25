package com.appdynamics.extensions.wordcounter;

import com.appdynamics.extensions.AMonitorTaskRunnable;
import com.appdynamics.extensions.MetricWriteHelper;

public class WordCounterTask implements AMonitorTaskRunnable {

    private MetricWriteHelper metricWriteHelper;
    private String filePath;
    private String fileName;

     WordCounterTask(String filePath, String fileName, MetricWriteHelper metricWriteHelper) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.metricWriteHelper = metricWriteHelper;
    }

    @Override
    public void run() {
        WordCounterHandler wordcounthandler = new WordCounterHandler();
        metricWriteHelper.transformAndPrintMetrics(wordcounthandler.populateAndPrintStats(filePath, fileName));
    }

    @Override
    public void onTaskComplete() {

    }
}
