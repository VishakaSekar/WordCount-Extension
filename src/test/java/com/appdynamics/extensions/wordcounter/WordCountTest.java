package com.appdynamics.extensions.wordcounter;

import com.appdynamics.extensions.MetricWriteHelper;
import com.appdynamics.extensions.metrics.Metric;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WordCountTest {

    private MetricWriteHelper metricWriteHelper = mock(MetricWriteHelper.class);

    @Test
    public void validWordsAndTheirCounts(){
        String filePath = "/Users/vishaka.sekar/AppDynamics/Sample1.txt";
        String fileName = "FileA";

        WordCounterTask wordCounterTask = new WordCounterTask(filePath,fileName, metricWriteHelper);
        wordCounterTask.run();
        ArgumentCaptor<List> arguments = ArgumentCaptor.forClass(List.class);
        verify(metricWriteHelper).transformAndPrintMetrics(arguments.capture());

        for(List<Metric> list : arguments.getAllValues()){
            for(Metric metric: list) {
                org.junit.Assert.assertTrue(metric.getMetricName().contains("The"));
                org.junit.Assert.assertTrue(metric.getMetricValue().equals("4"));
            }

        }

        assertThat(arguments.getAllValues(), hasSize(1));




    }

}
