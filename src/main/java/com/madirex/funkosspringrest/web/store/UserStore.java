package com.madirex.funkosspringrest.web.store;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

/**
 * Clase UserStore
 */
@Getter
@Component("userSession")
@Scope(value = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserStore {
    @Setter
    private boolean logged = false;
    private int loginCount = 0;

    @Setter
    private Date lastLogin;

    /**
     * Método para incrementar el contador de logins
     */
    public void incrementLoginCount() {
        loginCount++;
    }
}