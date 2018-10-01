package com.grade.plugin;

import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description:
 * @Date: Created in 17:03 2018/9/15
 */
public class SecondFirstServiceTest {

    @Test
    public void firstPart() {

        new SecondService().firstPart("6", "7", "7");
        /*double[] a = new double[]{1,2,28,5,1,5,21,5,5,51,5,5,15};
        List<Double> aa = new ArrayList<>();
        for (int i = 0; i< 10; i++){
            aa.add((double) (i+2));
        }
        System.out.println(aa.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
*/
    }

}