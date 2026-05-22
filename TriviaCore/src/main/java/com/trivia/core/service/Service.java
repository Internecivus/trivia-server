package com.trivia.core.service;

import com.trivia.core.exception.EntityNotFoundException;
import com.trivia.core.exception.InvalidInputException;
import com.trivia.core.utility.SortOrder;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.RoleType;
import org.modelmapper.internal.util.TypeResolver;

import javax.annotation.security.RolesAllowed;
import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * If we don't want to allow a certain method, we can override it in the subclass and leave it empty.
 */
@RolesAllowed(RoleType.Name.PRINCIPAL)
public abstract class Service<T> implements Repository<T> {
    @PersistenceContext(unitName = "TriviaDB")
    private EntityManager em;

    // TODO: Use annotations for some of this stuff.
    protected Integer PAGE_SIZE_DEFAULT = 20;
    protected Integer PAGE_SIZE_MAX = 100;
    protected SingularAttribute<T, ?> DEFAULT_SORT_COLUMN = null;
    protected Set<SingularAttribute<T, ?>> SORTABLE_COLUMNS = null;
    protected Set<SingularAttribute<T, ?>> SEARCHABLE_COLUMNS = null;

    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public Service() {
        entityClass = (Class<T>) TypeResolver.resolveArgument(this.getClass(), Service.class); // Magic.
    }

    public Object getEntityGraph(EntityView... entityViews) {
        if (entityViews != null && entityViews.length > 0) {
            return em.getEntityGraph(entityViews[0].getName());
        }
        return null;
    }

    public T findById(Object id, EntityView... entityViews) {
        T entity = getById(id, entityViews);
        if (entity == null) throw new EntityNotFoundException();
        return entity;
    }

    public T getById(Object id, EntityView... entityViews) {
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", getEntityGraph(entityViews));

        T entity = em.find(entityClass, id, hints);
        return entity;
    }

//    public <V, E> T getByFieldWithElement(SingularAttribute<T, V> field, V value, List<Attribute<T, E>> fetches, String... entityGraphs) {
//        T entity;
//        CriteriaBuilder builder = em.getCriteriaBuilder();
//        CriteriaQuery<T> query = builder.createQuery(entityClass);
//        Root<T> root = query.from(entityClass);
//
//        // TODO: Not sure if we want this to be optimised (it reuses the query if the param is the same type)
//        // for all fields because of security concerns.
//        ParameterExpression<V> parameter = builder.parameter(field.getJavaType(), field.getName());
//        query.select(root).where(builder.equal(root.get(field), parameter));
//
//        if (fetches != null & fetches.size() > 0) {
//            for (Attribute<T, E> fetch : fetches) {
//                root.fetch(fetch.getName(), JoinType.INNER);
//            }
//        }
//
//        TypedQuery<T> typedQuery = em.createQuery(query).setParameter(field.getName(), value);
//        typedQuery.setHint("javax.persistence.fetchgraph", getEntityGraph(entityGraphs));
//
//        /**
//         * The JPA API really sucks regarding this. The NoResultException is actually the way getSingleResult()
//         * is supposed to alert us no entity has been found.
//         */
//        try {
//            entity = typedQuery.getSingleResult();
//        }
//        catch (NoResultException e) {
//            entity = null;
//        }
//
//        return entity;
//    }

    public <V> T getByField(SingularAttribute<T, V> field, V value, EntityView... entityViews) {
        T entity;
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        // Not sure if we want this to be optimised (it reuses the query if the param is the same type)
        // for all fields because of security concerns.
        ParameterExpression<V> parameter = builder.parameter(field.getJavaType(), field.getName());
        query.select(root).where(builder.equal(root.get(field), parameter));
        TypedQuery<T> typedQuery = em.createQuery(query).setParameter(field.getName(), value);

        typedQuery.setHint("javax.persistence.fetchgraph", getEntityGraph(entityViews));

        /**
         * The JPA API really sucks regarding this. The NoResultException is actually the way getSingleResult()
         * is supposed to alert us no entity has been found.
         */
        try {
            entity = typedQuery.getSingleResult();
        }
        catch (NoResultException e) {
            entity = null;
        }

        return entity;
    }

    public <V> T findByField(SingularAttribute<T, V> field, V value, EntityView... entityViews) {
        T entity = getByField(field, value, entityViews);
        if (entity == null) throw new EntityNotFoundException();
        return entity;
    }

    public T update(T updatedEntity) {
        findById(getIdFieldOf(updatedEntity));
        em.merge(updatedEntity);
        em.flush();
        return updatedEntity;
    }

    public void deleteById(Object id) {
        T entity = findById(id);
        em.remove(entity);
        em.flush();
    }

    public T create(T entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    public Long count() {
        Long count;
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<T> root = query.from(entityClass);
        query.select(builder.count(root));
        TypedQuery<Long> typedQuery = em.createQuery(query);
        count = typedQuery.getSingleResult();

        return count;
    }

    public List<T> findAll(int pageCurrent, int pageSize, String sortColumn, SortOrder sortOrder, String searchString, EntityView... entityViews) {
        /* Init */
        List<T> entities;
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root);

        /* Sort */
        Path<?> path = getSortPath(sortColumn, root);
        if (sortOrder == null) sortOrder = SortOrder.DEFAULT;
        if (path != null) {
            switch(sortOrder) {
                case ASCENDING:
                    query.orderBy(builder.asc(path));
                    break;
                case DESCENDING:
                    query.orderBy(builder.desc(path));
                    break;
                case DEFAULT:
                    query.orderBy(builder.desc(path));
                    break;
            }
        }

        /* Filter */
        Predicate predicate = getFilter(searchString, builder, root);
        if (predicate != null) query.where(predicate);

        /* Pagination */
        TypedQuery<T> typedQuery = em.createQuery(query);
        //TODO: TEMPORARY - read below
        lastCount = typedQuery.getResultList().size();
        pageSize = (pageSize > 0 && pageSize <= PAGE_SIZE_MAX) ? pageSize : PAGE_SIZE_DEFAULT;
        typedQuery.setMaxResults(pageSize);
        pageCurrent = (pageCurrent > 0) ? pageCurrent : 1;
        typedQuery.setFirstResult(pageCurrent * pageSize - pageSize);

        typedQuery.setHint("javax.persistence.fetchgraph", getEntityGraph(entityViews));
        entities = typedQuery.getResultList();
        return entities;
    }

    // TODO: This method is kinda messy, for example it does not alert us if the searchable columns
    // are not for a supported type.
    @SuppressWarnings("unchecked")
    protected Predicate getFilter(String searchString, CriteriaBuilder builder, Root<T> root) {
        if (searchString == null || searchString.trim().length() < 1) {
            return null;
        }
        if (SORTABLE_COLUMNS == null || SEARCHABLE_COLUMNS.isEmpty()) {
            throw new InvalidInputException("No searchable fields found, but search initiated.");
        }

        Predicate predicates = builder.disjunction();
        for (SingularAttribute<T, ?> searchableField : SEARCHABLE_COLUMNS) {
            Expression<?> path = root.get(searchableField);
            Predicate predicate = null;

            Class<?> fieldType = searchableField.getJavaType();
            if (fieldType.equals(Integer.class)) {
                if (searchString.chars().allMatch(Character::isDigit)) {
                    predicate = builder.equal(path, Integer.parseInt(searchString));
                }
            }
            else if (fieldType.equals(Double.class)) {
                if (Pattern.matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$", searchString)) {
                    predicate = builder.equal(path, Double.valueOf(searchString));
                }
            }
            else if (fieldType.equals(String.class)) {
                predicate = builder.like((Expression<String>) path, "%" + searchString + "%");
            }

            if (predicate != null) {
                predicates = builder.or(predicates, predicate);
            }
        }
        return predicates;
    }

    protected Path<?> getSortPath(String column, Root<T> root) {
        if (column == null && DEFAULT_SORT_COLUMN != null) {
            return root.get(DEFAULT_SORT_COLUMN);
        }
        if (SORTABLE_COLUMNS == null || SORTABLE_COLUMNS.isEmpty()) {
            throw new InvalidInputException("No sortable fields found, but sort initiated.");
        }

        for (SingularAttribute<T, ?> sortableField : SORTABLE_COLUMNS) {
            if (sortableField.getName().equals(column)) {
                return root.get(sortableField);
            }
        }

        throw new InvalidInputException(String.format("No sortable column matching '%s' found.", column));
    }

    private Object getIdFieldOf(Object entity) {
        return em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
    }

    //TODO: TEMPORARY - This is crazy. Wrap the list inside a data structure (e.g. EntityPage -- has page out of x, randomized, sort field, sort order, count, etc.) to fix this.
    private int lastCount; public void setLastCount(int lastCount) {
        this.lastCount = lastCount;
    }public int getLastCount() {
        return lastCount;
    }
}