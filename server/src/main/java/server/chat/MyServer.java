package server.chat;

import ClientServer.Command;
import server.chat.auto.AuthService;
import server.chat.auto.BaseAuthService;
import server.chat.handler.ClientHandler;
import server.chat.auto.AuthService;
import server.chat.auto.BaseAuthService;
import server.chat.handler.ClientHandler;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServer {

    private final ServerSocket serverSocket;
    private final AuthService authService;
    private final List<ClientHandler> clients = new ArrayList<>();
    private ExecutorService service;
    public static final Logger logger = Logger.getLogger("");

    public MyServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.authService = new BaseAuthService();

    }

public void start() throws IOException {
        logger.log(Level.INFO,"Сервер запущен");
        //System.out.println("Сервер запущен!");
        service = Executors.newFixedThreadPool(5);
        try {
        while (true) {
        waitAndProcessNewClientConnection();
        }
        } catch (IOException e) {
                logger.log(Level.SEVERE, "Ошибка создания нового подключения!");
       // System.out.println("Ошибка создания нового подключения");
        e.printStackTrace();
        } finally {
        serverSocket.close();
        }
        }

private void waitAndProcessNewClientConnection() throws IOException {
            logger.log(Level.INFO, "Ожидание пользователя...");
       // System.out.println("Ожидание пользователя...");
        Socket clientSocket = serverSocket.accept();
        logger.log(Level.INFO, "Клиент подключился!");
       // System.out.println("Клиент подключился!");
        service.execute(()-> {
                try {
                        processClientConnection(clientSocket);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        });
        }

private void processClientConnection(Socket clientSocket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        clientHandler.handle();
        }

public AuthService getAuthService() {
        return authService;
        }

public synchronized boolean isUsernameBusy(String clientUsername) {
        for (ClientHandler client : clients) {
        if (client.getClientUsername().equals(clientUsername)) {
        return true;
        }
        }
        return false;
        }

public synchronized void subscribe(ClientHandler clientHandler) throws IOException {
        clients.add(clientHandler);
        logger.log(Level.INFO, clientHandler.getClientUsername()+" подключился к чату");
        List<String> usernames = getAllUsernames();
        broadcastMessage(null, Command.updateUsersListCommand(usernames));
        }

public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        for (ClientHandler client : clients) {
        usernames.add(client.getClientUsername());
        }
        return usernames;
        }

public synchronized void unSubscribe(ClientHandler clientHandler) throws IOException {
        logger.log(Level.INFO, clientHandler.getClientUsername()+" вышел из чата");
        clients.remove(clientHandler);
        List<String> usernames = getAllUsernames();
        broadcastMessage(null, Command.updateUsersListCommand(usernames));
        }

public synchronized void broadcastMessage(ClientHandler sender, Command command) throws IOException {
        for (ClientHandler client : clients) {
        if (client == sender) {
        continue;
        }
        client.sendMessage(command);

        }
        }

public synchronized void sendPrivateMessage(String recipient, Command command) throws IOException {
        for (ClientHandler client : clients) {
        if (client.getClientUsername().equals(recipient)) {
        client.sendMessage(command);
        break;
        }
        }
        }

}