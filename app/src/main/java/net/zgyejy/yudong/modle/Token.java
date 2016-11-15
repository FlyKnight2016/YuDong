package net.zgyejy.yudong.modle;

/**
 * 用户令牌的类
 * Created by Administrator on 2016/11/11 0011.
 */

public class Token {
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                '}';
    }
}
