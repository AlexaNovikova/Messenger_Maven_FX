package server.chat.handler;

public class MyRunTimeException extends RuntimeException {

    @Override
    public void printStackTrace() {
        System.out.println("Время ожидания истекло.");
        super.printStackTrace();
    }

}
