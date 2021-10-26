import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestArrayMethod1 {

    ArrMethods arrMethods;
int[] result;
int[] initialArr;


public TestArrayMethod1(int[] result, int[] initialArr){
    this.initialArr=initialArr;
    this.result=result;
}
    @Before
    public void prepareArr(){
        arrMethods= new ArrMethods();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {new int[]{1,7},new int[]{1,2,4,4,2,3,4,1,7}},
                {new int[]{1,5,7},new int[]{1,2,4,1,5,7}},
                {new int[]{},new int[]{1,2,4,4,2,3,4}},
                {new int[]{8,7,6},new int[]{4,8,7,6}},
        });
    }

    @Test
    public void testMethod1(){
        Assert.assertArrayEquals(result,ArrMethods.createArrAfterLastFour(initialArr));
    }

    @Test(expected = RuntimeException.class)
    public void testMethodException(){
     result=new int[]{1,2,3};
     initialArr=new int[]{5,6,7,8};
        Assert.assertArrayEquals(result,ArrMethods.createArrAfterLastFour(initialArr));
    }


}
