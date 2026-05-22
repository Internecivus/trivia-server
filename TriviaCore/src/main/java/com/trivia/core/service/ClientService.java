package com.trivia.core.service;

import com.trivia.core.exception.InvalidCredentialException;
import com.trivia.core.exception.NotAuthorizedException;
import com.trivia.core.security.Cryptography;
import com.trivia.core.utility.Generator;
import com.trivia.core.utility.SortOrder;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.*;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

// TODO: Implement a limit of how much clients a provider can have.
@Stateless
@RolesAllowed({RoleType.Name.PROVIDER, RoleType.Name.MODERATOR})
public class ClientService extends Service<Client> {
    @PersistenceContext(unitName = "TriviaDB")
    private EntityManager em;
    private @Resource SessionContext sessionContext;
    private @Inject Logger logger;
    private @Inject UserService userService;

    public ClientService() {
        super.DEFAULT_SORT_COLUMN = Client_.dateCreated;
        super.SEARCHABLE_COLUMNS = SORTABLE_COLUMNS = new HashSet<>(Arrays.asList(Client_.dateCreated, Client_.id));
    }

    private Client createFor(User provider) {
        Client newClient = new Client();
        String apiKey;

        do {
            apiKey = Generator.generateSecureRandomString(Cryptography.API_KEY_LENGTH);
        }
        while (getByField(Client_.apiKey, apiKey) != null); // We could've easily have left this check out since the BLOB is large enough.
        String apiSecret = Generator.generateSecureRandomString(Cryptography.API_KEY_LENGTH);

        newClient.setApiKey(apiKey);
        newClient.setUser(provider);
        newClient.setApiSecret(Cryptography.hashMessage(apiSecret));

        // A trick to return the secret before hashing.
        super.create(newClient);
        em.detach(newClient);
        newClient.setApiSecret(apiSecret);

        logger.info("Client id: {} was CREATED by user id: {}", newClient.getId(), sessionContext.getCallerPrincipal().getName());

        return newClient;
    }

    public String generateNewAPISecret(Client client) {
        if (!sessionContext.isCallerInRole(RoleType.Name.ADMIN)) {
            User user = userService.getByField(User_.name, sessionContext.getCallerPrincipal().getName());
            if (!user.isOwnerOf(client)) throw new NotAuthorizedException();
        }

        String apiSecret = Generator.generateSecureRandomString(Cryptography.API_KEY_LENGTH);
        client.setApiSecret(Cryptography.hashMessage(apiSecret));
        super.update(client);

        return apiSecret;
    }

    // Register for the current user if they are a provider.
    public Client registerForCurrent() {
        User user = userService.findByField(User_.name, sessionContext.getCallerPrincipal().getName());
        return createFor(user);
    }

    // Create for a specific provider.
    @PermitAll
    public Client registerFor(String providerKey, String providerSecret) {
        User provider = userService.validateProvider(providerKey, providerSecret);
        return createFor(provider);
    }

    // Admins don't need to know the secret.
    @RolesAllowed(RoleType.Name.ADMIN)
    public Client registerFor(String providerKey) {
        User provider = userService.findByField(User_.providerKey, providerKey);
        return createFor(provider);
    }

    @PermitAll
    public Client validateCredential(String apiKey, String apiSecret) {
        Client user = findByField(Client_.apiKey, apiKey);
        if (Cryptography.validateMessage(apiSecret, user.getApiSecret())) {
            return user;
        }
        else {
            throw new InvalidCredentialException();
        }
    }

    @Override
    public Client update(Client updatedClient) {
        Client client = findById(updatedClient.getId());
        if (!sessionContext.isCallerInRole(RoleType.Name.ADMIN)) {
            User user = userService.getByField(User_.name, sessionContext.getCallerPrincipal().getName());
            if (!user.isOwnerOf(client)) throw new NotAuthorizedException();
        }

        em.merge(updatedClient);
        em.flush();
        logger.info("Client id: {} was UPDATED by user id: {}", updatedClient.getId(), sessionContext.getCallerPrincipal().getName());

        return updatedClient;
    }

    @Override
    public void deleteById(Object id) {
        Client client = findById(id);
        if (!sessionContext.isCallerInRole(RoleType.Name.ADMIN)) {
            User user = userService.getByField(User_.name, sessionContext.getCallerPrincipal().getName());
            if (!user.isOwnerOf(client)) throw new NotAuthorizedException();
        }

        em.remove(client);
        em.flush();
        logger.info("Client id: {} was DELETED by user id: {}", id, sessionContext.getCallerPrincipal().getName());
    }

    @Override // TODO: Provides should find only their own!
    public List<Client> findAll(int pageCurrent, int pageSize, String sortColumn, SortOrder sortOrder, String searchString, EntityView... entityViews) {
        return super.findAll(pageCurrent, pageSize, sortColumn, sortOrder, searchString, entityViews);
    }
}
