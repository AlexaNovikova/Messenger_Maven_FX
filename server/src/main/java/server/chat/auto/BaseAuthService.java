package server.chat.auto;

import server.chat.User;

import java.sql.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {

//    User user1 = new User("user1", "1111", "Алекс");
//    User user2 = new User("user2", "2222", "Павел");
//    User user3 = new User("user3", "3333", "Лев");

  //  List<User> clients = new ArrayList<>();

    private Statement statement;
    private Connection connection;

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:Chat_Users.db");

    }


    public String getUsernameByLoginAndPassword(String login, String password) throws ClassNotFoundException {

//        clients.add(user1);
//        clients.add(user2);
//        clients.add(user3);
        try {
            connect();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            statement = connection.createStatement();
            ResultSet rez = statement.executeQuery("SELECT nick FROM users WHERE login='" + login + "' AND password='" + password + "';");
            //ResultSet rez = statement.executeQuery("SELECT nick FROM users WHERE login='user1';");

            String nick=rez.getString("nick");
            System.out.println(nick);
            return nick;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


//        for (User client : clients) {
//            if(client.getLogin().equals(login) & client.getPassword().equals(password) ) {
//                return client.getUsername();
//            }
//        }
        return null;
     }
}
