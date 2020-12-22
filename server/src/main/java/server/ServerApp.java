package server;

import com.sun.javafx.binding.StringFormatter;
import server.chat.MyServer;
import server.chat.MyServer;

import java.io.IOException;
import java.util.logging.*;


public class ServerApp {
    private static final int DEFAULT_PORT = 8189;
    public static final Logger logger = Logger.getLogger("");


    public static void main(String[] args)  {

         logger.setLevel(Level.FINE);

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("Level: %s, Message: %s\n", record.getLevel(), record.getMessage());
            }
        });

        consoleHandler.setLevel(Level.FINE);
        logger.addHandler(consoleHandler);
        logger.removeHandler(logger.getHandlers()[0]);

        int port = DEFAULT_PORT;

        if(args.length != 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            new MyServer(port).start();
        } catch (IOException e) {
            System.out.println("Ошибка!");
            logger.log(Level.SEVERE,"Ошибка при запуске сервера");
            e.printStackTrace();
            System.exit(1);
        }


    }

}
