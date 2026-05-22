package com.trivia.admin.security.authentication;

import com.trivia.core.exception.BusinessException;
import com.trivia.core.exception.CredentialException;
import com.trivia.core.service.UserService;
import com.trivia.persistence.entity.RoleType;
import com.trivia.persistence.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Set;


@ApplicationScoped
public class UserIdentityStore implements IdentityStore {
    @Inject private UserService userService;

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        User user;

        String username = credential.getCaller();
        String password = credential.getPasswordAsString();

        try {
            user = userService.validateCredential(username, password);
        }
        catch (CredentialException e) {
            return CredentialValidationResult.INVALID_RESULT;
        }
        catch (BusinessException e) {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }

        Set<String> roles = user.getRolesAsStrings();
        roles.add(RoleType.Name.USER);
        roles.add(RoleType.Name.PRINCIPAL);

        return new CredentialValidationResult(new UserCallerPrincipal(user), roles);
    }
}