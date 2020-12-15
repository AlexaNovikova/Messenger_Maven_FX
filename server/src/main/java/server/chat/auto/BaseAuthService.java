package server.chat.auto;

import java.sql.*;

public class BaseAuthService implements AuthService {

//    User user1 = new User("user1", "1111", "Алекс");
//    User user2 = new User("user2", "2222", "Павел");
//    User user3 = new User("user3", "3333", "Лев");

  //  List<User> clients = new ArrayList<>();

    private Statement statement;
    private Connection connection;
    private  PreparedStatement regPrepStm;
    private PreparedStatement authPrepStm;
    private PreparedStatement changeNickStm;

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:Chat_Users.db");
     //   statement = connection.createStatement();
    }

    public void disconnect(){
//        try {
//         regPrepStm.close();
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

public Integer registration (String nick, String login, String password) {
    try {
        connect();
       // statement = connection.createStatement();
        regStatement();
        regPrepStm.setString(1, nick);
        regPrepStm.setString(2,login);
        regPrepStm.setString(3,password);
        int rez = regPrepStm.executeUpdate();
        regPrepStm.close();;
        return rez;
    } catch (SQLException|ClassNotFoundException e) {
        e.printStackTrace();
    }
    finally {
        disconnect();
    }
    return -1;

}
    public Integer changeNick(String login, String password, String oldNick, String newNick){
        try {
            connect();
            // statement = connection.createStatement();
            PreparedStatement search = connection.prepareStatement("SELECT * from users WHERE nick =?;");
            search.setString(1,newNick);
            ResultSet resultSet=search.executeQuery();
               if (resultSet.next()) {String name = resultSet.toString();
             //   System.out.println(name);
                search.close();
                resultSet.close();
                return -2;
            }

            else {
                changeNickStatement();
                changeNickStm.setString(1, newNick);
                changeNickStm.setString(2, oldNick);
                changeNickStm.setString(3, login);
                changeNickStm.setString(4, password);

                Integer rez = changeNickStm.executeUpdate();
                changeNickStm.close();
                ;
                return rez;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            disconnect();
        }
        return -1;

    }

public void regStatement () throws SQLException {
       regPrepStm = connection.prepareStatement("INSERT INTO users (nick,login,password) VALUES (?,?,?);");
}
public void authStatement () throws SQLException {
        authPrepStm = connection.prepareStatement("SELECT nick FROM users WHERE login= ? AND password= ?;");
}

    public void changeNickStatement () throws SQLException {
        changeNickStm= connection.prepareStatement("UPDATE users SET nick=? WHERE nick =? AND login =? AND password =?;");
    }


    public String getUsernameByLoginAndPassword(String login, String password){

//        clients.add(user1);
//        clients.add(user2);
//        clients.add(user3);
        try {
            connect();
        } catch (SQLException|ClassNotFoundException e) {
           e.printStackTrace();
        }
        try {
//            statement = connection.createStatement();
//            ResultSet rez = statement.executeQuery("SELECT nick FROM users WHERE login='" + login + "' AND password='" + password + "';");
            //ResultSet rez = statement.executeQuery("SELECT nick FROM users WHERE login='user1';");

            authStatement();
            authPrepStm.setString(1,login);
            authPrepStm.setString(2,password);
            ResultSet rez = authPrepStm.executeQuery();
            String nick=rez.getString("nick");
            System.out.println(nick);
           authPrepStm.close();
            return nick;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            disconnect();
        }


//        for (User client : clients) {
//            if(client.getLogin().equals(login) & client.getPassword().equals(password) ) {
//                return client.getUsername();
//            }
//        }
        return null;
     }
}
