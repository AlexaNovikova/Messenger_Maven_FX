package lesson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {
  static volatile int p = 1;
    static Object mon = new Object();
   final static int kol= 5;

    public static void main(String[] args) {


       new Thread(()->{
            try { for (int i=0; i<kol; i++){
              synchronized (mon){
                  while (p!=1) {
                      mon.wait();
                      }
                  System.out.println("A");
                  p=2;
                  mon.notifyAll();
                      }
                  }
              }
            catch (InterruptedException e) {
                e.printStackTrace();
        }
      }).start();


    new Thread(()->{
        try { for (int i=0; i<kol; i++){
            synchronized (mon){
                while (p!=2) {
                    mon.wait();
                }
                System.out.println("B");
                p=3;
                mon.notifyAll();
            }
        }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }).start();



  new Thread(()->
    {  try { for (int i=0; i<kol; i++){
          synchronized (mon){
              while (p!=3) {
                  mon.wait();
              }
              System.out.println("C");
              p=1;
              mon.notifyAll();
          }
      }
      }
      catch (InterruptedException e) {
          e.printStackTrace();
      }
      }).start();

    }
}
