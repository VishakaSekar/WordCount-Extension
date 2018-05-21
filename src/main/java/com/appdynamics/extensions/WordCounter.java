package com.appdynamics.extensions;

import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import org.yaml.snakeyaml.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class WordCounter extends AManagedMonitor {
    static String file ="/Users/vishaka.sekar/AppDynamics/WordCountExtension/src/main/resources/conf/config.yml";
    private final static String metricPrefix = "Custom Metrics|WordCount|Status|";

    public static String getMetricPrefix() {
        return metricPrefix;
    }

    private Map<String,String> resultMap = new HashMap<String,String>();
    protected volatile String host;
    protected volatile String port;
    protected volatile String location;


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }




    public static void main(String arg[]){


    Map<String, Integer> map;
    map = counter(file);

    for(Map.Entry entry : map.entrySet()){
        System.out.println(entry.getKey() +":"+ entry.getValue());

    }

    }

     static Map<String, Integer> counter(String file){

        String line = null;
        Map<String, Integer> resultMap =
                new HashMap<String, Integer>();
        String filePath= null;

        Yaml yaml = new Yaml();


        try{
            InputStream ios = new FileInputStream(new File(file));
            Map<String, Map<String, String>> result =  (Map)yaml.load(ios);

            for(Object key : result.keySet()){

                Map<String, String> subValues = result.get(key);

                for(String subKeys: subValues.keySet()){
                    System.out.println(subKeys+ ":"+subValues.get(subKeys));

                    if(subKeys.equalsIgnoreCase("-filePath")){
                        filePath = (subValues.get(subKeys));

                    }
                }


            }

        }catch(Exception e){
            System.out.println(e);
        }



        try{
            FileReader fileReader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fileReader);


            while((line = br.readLine())!=null){
            String[] array = (line.split("\\s+"));
               for(String str : array){

                  if(resultMap.containsKey(str)){
                      resultMap.put(str, resultMap.put(str, resultMap.get(str)) + 1 );
                   }
                   else
                      resultMap.put(str, 1);
               }



            }
        }catch(FileNotFoundException fe ){
            System.out.print(fe);
        } catch (IOException ioe){
            System.out.print(ioe);

        }

        return resultMap;

    }


    @Override
    public com.singularity.ee.agent.systemagent.api.TaskOutput execute(java.util.Map<String, String> arg0, com.singularity.ee.agent.systemagent.api.TaskExecutionContext arg1) {
        try{


            //calls the counter method
            Map<String, Integer> resultMap;
            resultMap = counter(file);

            //getting metrics from the resultMap

            for(Map.Entry entry : resultMap.entrySet()){
                printMetric(entry.getKey().toString(), entry.getValue(),
                        MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
                        MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
                        MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);
            }





        }catch(Exception e){
            return new TaskOutput("Error: " + e);
        }

    return new TaskOutput("WordCount Metric Upload Complete");
    }


    public void printMetric(String metricName, Object metricValue, String aggregation, String timeRollup, String cluster)
    {
        MetricWriter metricWriter = getMetricWriter(getMetricPrefix() + metricName,
                aggregation,
                timeRollup,
                cluster
        );

        metricWriter.printMetric(String.valueOf(metricValue));
    }


}
