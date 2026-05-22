package com.trivia.api.security.authentication;

import com.trivia.persistence.entity.Client;

import javax.security.enterprise.CallerPrincipal;


public class ClientCallerPrincipal extends CallerPrincipal {
    private final Client client;

    public ClientCallerPrincipal(Client client) {
        super(client.getApiKey());
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
