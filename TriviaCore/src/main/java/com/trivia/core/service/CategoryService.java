package com.trivia.core.service;

import com.trivia.core.utility.ImageUtil;
import com.trivia.core.utility.SortOrder;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.dto.client.CategoryClient;
import com.trivia.persistence.dto.client.ImageData;
import com.trivia.persistence.entity.Category;
import com.trivia.persistence.entity.Category_;
import com.trivia.persistence.entity.RoleType;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.InputStream;
import java.util.*;

@Stateless
@RolesAllowed(RoleType.Name.MODERATOR)
public class CategoryService extends Service<Category> {
    @PersistenceContext(unitName = "TriviaDB")
    private EntityManager em;
    private @Resource SessionContext sessionContext;
    private @Inject Logger logger;

    public CategoryService() {
        super.DEFAULT_SORT_COLUMN = Category_.dateCreated;
        super.SEARCHABLE_COLUMNS = SORTABLE_COLUMNS = new HashSet<>(Arrays.asList(Category_.id, Category_.dateCreated, Category_.name, Category_.description));
    }

    @RolesAllowed(RoleType.Name.PRINCIPAL)
    public Set<Category> getAll() {
        Set<Category> categoryList;
        TypedQuery<Category> query = em.createQuery("SELECT c from Category c", Category.class);

        categoryList = new HashSet<>(query.getResultList());

        return categoryList;
    }

    @RolesAllowed({RoleType.Name.PRINCIPAL})
    public Set<String> getAllNames() {
        Set<Category> categoryList = getAll();
        Set<String> namesList = new HashSet<>();

        for (Category category : categoryList) {
            namesList.add(category.getName());
        }
        return namesList;
    }

    public Category create(Category newCategory, InputStream imageStream) {
        String imagePath = ImageUtil.saveImage(imageStream);
        newCategory.setImage(imagePath);
        return super.create(newCategory);
    }

    @Override
    @Deprecated
    public Category create(Category newCategory) {
        throw new IllegalStateException("A category needs to use the InputStream create method, as it needs to have an image!");
    }

    @Override
    @RolesAllowed({RoleType.Name.CONTRIBUTOR})
    public List<Category> findAll(int pageCurrent, int pageSize, String sortColumn, SortOrder sortOrder, String searchString, EntityView... entityViews) {
        return super.findAll(pageCurrent, pageSize, sortColumn, sortOrder, searchString, entityViews);
    }

    @PermitAll
    public Collection<CategoryClient> toDto(Collection<Category> categories) {
        ModelMapper mapper = new ModelMapper();
        List<CategoryClient> categoriesDto = mapper.map(categories, new TypeToken<List<CategoryClient>>() {
        }.getType());

        for (int i = 0; i < categoriesDto.size(); i++) {
            String imagePath = new ArrayList<>(categories).get(i).getImage();
            if (imagePath != null) {
                categoriesDto.get(i).setImageData(new ImageData(imagePath, ImageUtil.getDateCreated(imagePath)));
            }
        }

        return categoriesDto;
    }

    @Override
    public void deleteById(Object id) {
        super.deleteById(id);
        logger.info("Category id: {} was DELETED by user: {}", id, sessionContext.getCallerPrincipal().getName());
    }

    public void update(Category updatedCategory, InputStream imageStream) {
        Category oldCategory = findById(updatedCategory.getId());
        ImageUtil.validateImagePath(oldCategory.getImage(), updatedCategory.getImage());

        // If there is already an image present (and there should be) and we are adding a new one, we need to delete it.
        if (oldCategory.getImage() != null) ImageUtil.deleteImage(oldCategory.getImage());

        // Create and set the new image.
        updatedCategory.setImage(ImageUtil.saveImage(imageStream));

        em.merge(updatedCategory);
        em.flush();
        logger.info("Category id: {} was UPDATED by user: {}", updatedCategory.getId(), sessionContext.getCallerPrincipal().getName());
    }

    @Override
    public Category update(Category updatedCategory) {
        Category oldCategory = findById(updatedCategory.getId());
        ImageUtil.validateImagePath(oldCategory.getImage(), updatedCategory.getImage());

        em.merge(updatedCategory);
        em.flush();
        logger.info("Category id: {} was UPDATED by user: {}", oldCategory.getId(), sessionContext.getCallerPrincipal().getName());

        return updatedCategory;
    }
}
