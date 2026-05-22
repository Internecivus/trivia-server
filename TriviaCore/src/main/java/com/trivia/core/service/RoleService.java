package com.trivia.core.service;

import com.trivia.persistence.entity.Role;
import com.trivia.persistence.entity.RoleType;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

@Stateless
// We are not inheriting from Service as we do NOT want all the methods.
@RolesAllowed({RoleType.Name.MODERATOR})
public class RoleService {
    @PersistenceContext(unitName = "TriviaDB")
    private EntityManager em;
    private @Resource SessionContext sessionContext;

    public RoleService() {}

    public Set<Role> getAll() {
        Set<Role> roleList;
        TypedQuery<Role> query = em.createQuery("SELECT r from Role r", Role.class);

        roleList = new HashSet<>(query.getResultList());

        return roleList;
    }
}
