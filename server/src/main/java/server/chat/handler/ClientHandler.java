package server.chat.handler;

import ClientServer.Command;
import ClientServer.CommandType;
import ClientServer.commands.*;
import ClientServer.commands.PrivateMessageCommandData;
import ClientServer.commands.PublicMessageCommandData;
import server.chat.*;
import server.chat.auto.AuthService;
import server.chat.auto.AuthService;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

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

//        new Thread(() -> {
        try {
             authentication();
             readMessage();
         }
catch (EOFException e){
   in.close();
   out.close();}
//
    }

    private void authentication() throws IOException {
        while (true){
            Command command = readCommand();

            if (command == null) {
                continue;
//                Set set = new TreeSet();
            }

            if (command.getType() == CommandType.AUTH) {

                boolean isSuccessAuth = false;
                try {
                    isSuccessAuth = processAuthCommand(command);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (isSuccessAuth) {
                    MyServer.logger.log(Level.INFO, "Авторизация прошла успешно");
                   break;

                }
                MyServer.logger.log(Level.INFO, "Авторизация не удалась");
            }

            if (command.getType() == CommandType.REGISTER) {

                boolean isSuccessReg = false;
                try {
                    isSuccessReg = processRegCommand(command);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
//                if (isSuccessReg) continue;

            }
            if (command.getType() == CommandType.NICK_CHANGE) {

                boolean isSuccessChange = false;
                try {
                    isSuccessChange= processChangeNickCommand(command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                if (isSuccessReg) continue;

            }

        }

    }

    private boolean processChangeNickCommand(Command command) throws IOException {
        NickChangeCommandData changeData = (NickChangeCommandData) command.getData();
        String login = changeData.getLogin();
        String password = changeData.getPassword();
        String oldNick = changeData.getOldNick();
        String newNick = changeData.getNewNick();

        List<String> usersOnline = myServer.getAllUsernames();
        for (String user: usersOnline) {
            if (oldNick.equals(user))
            {sendMessage(Command.nickChangeFailCommand("Пользователь с таким ником уже в чате!"));
            return false;}
            if (newNick.equals(user))
            {sendMessage(Command.nickChangeFailCommand("Ник занят!"));
            return false;}
        }
        AuthService authService = myServer.getAuthService();
        Integer resChange = authService.changeNick(login, password, oldNick, newNick);

        if (resChange == 1) {
            sendMessage(Command.nickChangeOKCommand());
            MyServer.logger.log(Level.INFO, "Пользователь успешно изменил ник");
            return true;
        }
        if (resChange == 0) {
            sendMessage(Command.nickChangeFailCommand("Не найден пользователь с такими данными!"));
            return false;
        }
        if (resChange == -2) {
            sendMessage(Command.nickChangeFailCommand("Ник занят!"));
            return false;
        }
        else {
            sendMessage(Command.nickChangeFailCommand("Ошибка."));
            MyServer.logger.log(Level.SEVERE, "Ошибка при смене ника.");
            return false;
        }

    }

    private boolean processAuthCommand(Command command) throws IOException, ClassNotFoundException {
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

            sendMessage(Command.authOkCommand(clientUsername, login));
            String message = String.format(">>> %s присоединился к чату", clientUsername);
            myServer.broadcastMessage(this, Command.messageInfoCommand(message, null));
            myServer.subscribe(this);
            return true;
        } else {
            sendMessage(Command.authErrorCommand("Логин или пароль не соответствуют действительности"));
            return false;
        }
    }

    private boolean processRegCommand(Command command) throws IOException, ClassNotFoundException {
      SendRegisterCommandData regData = (SendRegisterCommandData) command.getData();
        String nick = regData.getNick();
        String login = regData.getLogin();
        String password = regData.getPassword();

        AuthService authService = myServer.getAuthService();
        Integer resRegistration = authService.registration(nick,login,password);

        if ( resRegistration==1)
        {
                sendMessage(Command.regOKCommand());
                MyServer.logger.log(Level.INFO,"Регистрация прошла успешно");
                 return true;
            }
        if (resRegistration==0)
        {
            sendMessage(Command.regFailCommand("Пользователь с таким ником/паролем/логином уже зарегистрирован!"));
            return false;
        } else {
            sendMessage(Command.regFailCommand("Ошибка при регистрации."));
            MyServer.logger.log(Level.SEVERE,"Ошибка при регистрации.");
            return false;
        }
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Получен неизвестный объект";
            MyServer.logger.log(Level.SEVERE, "Получен неизвестный объект.");
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
                case END_CONNECTION_WANT:
                  //  sendMessage(Command.endConnectionFromServer(this.clientUsername));
                    myServer.unSubscribe(this);
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
