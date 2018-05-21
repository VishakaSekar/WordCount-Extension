package com.appdynamics.extensions;

import org.junit.Assert;
import org.junit.Test;
import java.util.Map;

public class TestCases {

    @Test
    public void notNull(){

        com.appdynamics.extensions.WordCounter wordcounter = new com.appdynamics.extensions.WordCounter();
        Map<String, Integer> map;



        map = wordcounter.counter(wordcounter.getFile());

        Assert.assertNotNull("The counter map should not be null" , map);



    }
}
