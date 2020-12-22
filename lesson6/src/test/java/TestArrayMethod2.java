import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

    @RunWith(Parameterized.class)
    public class TestArrayMethod2 {

        ArrMethods arrMethods;
        int[] mass;
        boolean result;


        public TestArrayMethod2(boolean result, int[] initialArr){
            this.mass=initialArr;
            this.result=result;
        }

        @Before
        public void prepareArr(){
            arrMethods= new ArrMethods();
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {false,new int[]{1,2,4,1,5,7}},
                    {false,new int[]{1,1,1,1,1}},
                    {false,new int []{4,4,4,4,4,4}},
                    {true,new int[]{1,1,1,1,1,4,4}},
                    {true,new int []{1,4,1,4,1,4}},
            });
        }


//        @Test
//        public void testMethod2(){
//            Assert.assertFalse(ArrMethods.hasFoursAndOnes(mass));
//        }
//
//
//        @Test
//        public  void testMethod2true(){
//            Assert.assertTrue(ArrMethods.hasFoursAndOnes(new int[]{1,1,1,4,4,4}));
//        }

        @Test
        public  void testAll(){
            Assert.assertArrayEquals(new boolean[]{result}, new boolean[]{ArrMethods.hasFoursAndOnes(mass)});
        }

    }


