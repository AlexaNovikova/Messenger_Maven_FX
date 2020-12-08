package server.chat.auto;

import server.chat.User;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {

    User user1 = new User("user1", "1111", "Алекс");
    User user2 = new User("user2", "2222", "Павел");
    User user3 = new User("user3", "3333", "Лев");

    List<User> clients = new ArrayList<>();

    public String getUsernameByLoginAndPassword(String login, String password) {
        clients.add(user1);
        clients.add(user2);
        clients.add(user3);

        for (User client : clients) {
            if(client.getLogin().equals(login) & client.getPassword().equals(password) ) {
                return client.getUsername();
            }
        }
        return null;
    }
}
