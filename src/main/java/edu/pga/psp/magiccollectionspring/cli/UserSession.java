package edu.pga.psp.magiccollectionspring.cli;

import org.springframework.stereotype.Component;

@Component
public class UserSession {
    private String authToken;
    private String username;

    public boolean isLoggedIn() {
        return authToken != null;
    }

    public void login(String username, String token) {
        this.username = username;
        this.authToken = token;
    }

    public void logout() {
        this.username = null;
        this.authToken = null;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
