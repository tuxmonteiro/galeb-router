package io.galeb.core.mapreduce;

import org.junit.Test;

public class NullMapReduceTest {

    private MapReduce mapReduce = new NullMapReduce();

    @Test
    public void getTimeOutTest() {
        assert(mapReduce.getTimeOut()).equals(-1L);
    }

    @Test
    public void containsTest() {
        assert(mapReduce.contains("")==false);
    }

    @Test
    public void reduceTest() {
        assert(mapReduce.reduce().isEmpty());
    }

}
