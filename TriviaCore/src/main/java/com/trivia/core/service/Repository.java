package com.trivia.core.service;

import com.trivia.core.utility.SortOrder;
import com.trivia.persistence.EntityView;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

/**
 * The difference between find and get methods is that find methods throw a EntityNotFound exception.
 */
public interface Repository<T> {
    T findById(Object id, EntityView... entityViews);
    T getById(Object id, EntityView... entityViews);

    <V> T findByField(SingularAttribute<T, V> field, V value, EntityView... entityViews);
    <V> T getByField(SingularAttribute<T, V> field, V value, EntityView... entityViews);

    T update(T updatedEntity);
    T create(T newEntity);

    void deleteById(Object id);

    List<T> findAll(int pageCurrent, int pageSize, String sortField, SortOrder sortOrder, String searchString, EntityView... entityViews);

    Long count();
}
