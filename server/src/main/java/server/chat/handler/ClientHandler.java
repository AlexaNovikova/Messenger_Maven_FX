package server.chat.handler;

import ClientServer.Command;
import ClientServer.CommandType;
import ClientServer.commands.*;
import ClientServer.commands.PrivateMessageCommandData;
import ClientServer.commands.PublicMessageCommandData;
import server.chat.*;
import server.chat.auto.AuthService;
import server.chat.auto.AuthService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {

    private final MyServer myServer;
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String clientUsername;

    public ClientHandler(MyServer myServer, Socket clientSocket) {
        this.myServer = myServer;
        this.clientSocket = clientSocket;
//        try {
//            clientSocket.setSoTimeout(1000);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }

    }

    public void handle() throws IOException {
        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());


        new Thread(() -> {
            try {
                if (authentication());
                 readMessage();
               }

            catch (IOException e) {
                System.out.println(e.getMessage());
            }
            catch (MyRunTimeException e){
                System.out.println("Время ожидания истекло!");
            }
        }).start();
    }

    private boolean authentication() throws IOException {
        long timeStart = System.currentTimeMillis();
        long timeEnd = timeStart+12000L;

      while(true){
          if (System.currentTimeMillis() > timeEnd){
              sendMessage(Command.errorCommand("Допустимое время ожидания истекло!"));
              throw new MyRunTimeException();

          }
            Command command = readCommand();

            if (command == null) {
                continue;
//                Set set = new TreeSet();
            }

            if (command.getType() == CommandType.AUTH) {

                boolean isSuccessAuth = processAuthCommand(command);
                if (isSuccessAuth) {
               //     break;
                 return true;
                }

            }
            else
            { sendMessage(Command.authErrorCommand("Ошибка авторизации"));

            }
        }

    }




    private boolean processAuthCommand(Command command) throws IOException {
        AuthCommandData cmdData = (AuthCommandData) command.getData();
        String login = cmdData.getLogin();
        String password = cmdData.getPassword();

        AuthService authService = myServer.getAuthService();
        this.clientUsername = authService.getUsernameByLoginAndPassword(login, password);
        if (clientUsername != null) {
            if (myServer.isUsernameBusy(clientUsername)) {
                sendMessage(Command.authErrorCommand("Логин уже используется"));
                return false;
            }

            sendMessage(Command.authOkCommand(clientUsername));
            String message = String.format(">>> %s присоединился к чату", clientUsername);
            myServer.broadcastMessage(this, Command.messageInfoCommand(message, null));
            myServer.subscribe(this);
            return true;
        } else {
            sendMessage(Command.authErrorCommand("Логин или пароль не соответствуют действительности"));
            return false;
        }
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Получен неизвестный объект";
            System.err.println(errorMessage);
            e.printStackTrace();
            return null;
        }
    }

    private void readMessage() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }

            switch (command.getType()) {
                case END:
                    return;
                case PUBLIC_MESSAGE: {
                    PublicMessageCommandData data = (PublicMessageCommandData) command.getData();
                    String message = data.getMessage();
                    String sender = data.getSender();
                    myServer.broadcastMessage(this, Command.messageInfoCommand(message, sender));
                    break;
                }
                case PRIVATE_MESSAGE:
                    PrivateMessageCommandData data = (PrivateMessageCommandData) command.getData();
                    String recipient = data.getReceiver();
                    String message = data.getMessage();
                    myServer.sendPrivateMessage(recipient, Command.messageInfoCommand(message, this.clientUsername));
                    break;
                default:
                    String errorMessage = "Неизвестный тип команды" + command.getType();
                    System.err.println(errorMessage);
                    sendMessage(Command.errorCommand(errorMessage));
            }
        }
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void sendMessage(Command command) throws IOException {
        out.writeObject(command);
    }

}
