package ClientServer;

public enum CommandType {
    AUTH,
    AUTH_OK,
    AUTH_ERROR,
    PRIVATE_MESSAGE,
    PUBLIC_MESSAGE,
    INFO_MESSAGE,
    ERROR,
    END,
    UPDATE_USERS_LIST,
    REGISTER,
    REG_OK,
    REG_ERROR,
    NICK_CHANGE,
    CHANGE_OK,
    CHANGE_FAIL,
    END_CONNECTION_WANT,
    END_CONNECTION_OK

}
