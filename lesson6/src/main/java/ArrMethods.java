//1. Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
//        Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов,
//        идущих после последней четверки. Входной массив должен содержать хотя бы одну четверку,
//        иначе в методе необходимо выбросить RuntimeException.
//        Написать набор тестов для этого метода (по 3-4 варианта входных данных).
//        Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
//
//        2. Написать метод, который проверяет состав массива из чисел 1 и 4.
//        Если в нем нет хоть одной четверки или единицы, то метод вернет false;
//        Если есть элементы отличные от 1 и 4 то вернуть false.
//        Написать набор тестов для этого метода (по 3-4 варианта входных данных).
//        [ 1 1 1 4 4 1 4 4 ] -> true
//        [ 1 1 1 1 1 1 ] -> false
//        [ 4 4 4 4 ] -> false
//        [ 1 4 4 1 1 4 3 ] -> false
//
//        3. * Добавить на серверную сторону сетевого чата логирование событий (сервер запущен,
//        произошла ошибка, клиент подключился, клиент прислал сообщение/команду).

import java.util.Arrays;

public class ArrMethods {

    public static void main(String[] args) {

        int[] array = {1,2,4,4,2,3,4,1,7};
        int[] array2 = {1,1,1,1};
        int[] array3 = {4,5,4,6,7,4};
        System.out.println(Arrays.toString(array));
        try{
        int[] result = createArrAfterLastFour(array);
        System.out.println(Arrays.toString(result));
        }
        catch (RuntimeException e){
            // e.printStackTrace();
            System.out.println("В массиве нет 4");
        }
        System.out.println(Arrays.toString(array2));
        try{
            int[] result2 = createArrAfterLastFour(array2);
            System.out.println(Arrays.toString(result2));
        }
        catch (RuntimeException e){
            //e.printStackTrace();
            System.out.println("В массиве нет 4");
        }
        System.out.println(Arrays.toString(array3));
        try{
            int[] result3 = createArrAfterLastFour(array3);
            System.out.println(Arrays.toString(result3));
        }
        catch (RuntimeException e){
           // e.printStackTrace();
            System.out.println("В массиве нет 4");
        }

        int [] arrWithFoursAndOnes = {1,1,1,4,4,4,1,1,1};
        int [] arrWithOtherNumbers={1,4,5,6,7,7};
        int [] onlyOne ={1,1,1,1};
        int [] onlyFour= {4,4,4};
        System.out.println(Arrays.toString(arrWithFoursAndOnes));
        System.out.println(hasFoursAndOnes(arrWithFoursAndOnes));
        System.out.println(Arrays.toString(arrWithOtherNumbers));
        System.out.println(hasFoursAndOnes(arrWithOtherNumbers));
        System.out.println(Arrays.toString(onlyFour));
        System.out.println(hasFoursAndOnes(onlyFour));
        System.out.println(Arrays.toString(onlyOne));
        System.out.println(hasFoursAndOnes(onlyOne));
    }

    public static int[] createArrAfterLastFour(int[]arr){
        int positionOfLastFour=-1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i]==4) positionOfLastFour=i;
        }
        if (positionOfLastFour==-1){
            throw new RuntimeException();
        }
        positionOfLastFour++;
        int j=0;
        int[] arrayAfterFour=new int[arr.length-positionOfLastFour];
        for (int i = positionOfLastFour; i < arr.length; i++) {
            arrayAfterFour[j]=arr[i];
            j++;
        }
        return arrayAfterFour;
    }

  public static boolean hasFoursAndOnes( int[] arr){
        boolean hasOne=false;
        boolean hasFour = false;
      for (int i = 0; i < arr.length; i++) {
          if ((arr[i]!=1)&&(arr[i]!=4)){
              return false;
          }
          if ((arr[i]==1)) {
              hasOne=true;
          }
          if (arr[i]==4){
              hasFour=true;
          }
      }
      return hasFour&&hasOne;
  }
}
