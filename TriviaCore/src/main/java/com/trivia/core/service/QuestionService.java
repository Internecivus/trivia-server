package com.trivia.core.service;

import com.trivia.core.exception.InvalidInputException;
import com.trivia.core.utility.Generator;
import com.trivia.core.utility.ImageUtil;
import com.trivia.persistence.dto.client.ImageData;
import com.trivia.persistence.dto.client.QuestionClient;
import com.trivia.persistence.entity.*;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


// TODO: Lots of confusing trouble with imagePath...
@Stateless
@RolesAllowed(RoleType.Name.PRINCIPAL)
public class QuestionService extends Service<Question> {
    @PersistenceContext(unitName = "TriviaDB")
    private EntityManager em;
    private @Inject UserService userService;
    private @Inject Logger logger;
    private @Resource SessionContext sessionContext;

    private final static Integer PAGE_SIZE_RANDOM_DEFAULT = 10;
    private final static Integer PAGE_SIZE_RANDOM_MAX = 50;

    public QuestionService() {
        super.DEFAULT_SORT_COLUMN = Question_.dateCreated;
        super.SEARCHABLE_COLUMNS = SORTABLE_COLUMNS = new HashSet<>(Arrays.asList(
            Question_.id, Question_.answerFirst, Question_.question, Question_.dateLastModified,
            Question_.answerSecond, Question_.answerThird, Question_.answerFourth,
            Question_.answerCorrect, Question_.comment, Question_.dateCreated
        ));
    }

    @RolesAllowed(RoleType.Name.CLIENT)
    public List<Question> getRandomFromCategory(int size, String category) {
        if (category == null || category.trim().length() < 1) throw new InvalidInputException("No category specified.");
        if (size == 0) size = PAGE_SIZE_RANDOM_DEFAULT;
        if (size < 0) throw new InvalidInputException(String.format("Size must be a positive number (is %d).", size));
        if (size > PAGE_SIZE_RANDOM_MAX) throw new InvalidInputException(String.format(
                    "Size is over the maximum allowed size of %d (is %d).", PAGE_SIZE_RANDOM_MAX, size));

        List<Question> questions = new ArrayList<>();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Question> query = builder.createQuery(Question.class);
        Root<Question> root = query.from(Question.class);
        query.select(root);
        Join join = root.join(Question_.categories);
        query.where(builder.equal(join.get(Category_.name), category));

        TypedQuery<Question> typedQuery = em.createQuery(query);

        int count = typedQuery.getResultList().size();
        if (size > count) throw new InvalidInputException(String.format(
                    "Not enough questions matching category '%s' found (found only %d out of %d).", category, count, size));

        // Would this be faster with RAND()?
        int[] randomUniqueArray = Generator.generateRandomUniqueArray(size, count);
        for (int i = 0; i < randomUniqueArray.length; i++) {
            typedQuery.setFirstResult(randomUniqueArray[i]).setMaxResults(1);
            questions.addAll(typedQuery.getResultList()); // This logic is unfortunate.
        }
        return questions;
    }

    @RolesAllowed({RoleType.Name.CONTRIBUTOR})
    public void update(Question updatedQuestion, InputStream imageStream) {
        Question oldQuestion = findById(updatedQuestion.getId());
        ImageUtil.validateImagePath(oldQuestion.getImage(), updatedQuestion.getImage());

        // There is already an image present, and since we are adding a new one we need to delete it.
        if (oldQuestion.getImage() != null) {
            ImageUtil.deleteImage(oldQuestion.getImage());
        }

        // Create and set the new image.
        updatedQuestion.setImage(ImageUtil.saveImage(imageStream));

        em.merge(updatedQuestion);
        em.flush();
        logger.info("Question id: {} was UPDATED by user: {}", updatedQuestion.getId(), sessionContext.getCallerPrincipal().getName());
    }

    @Override
    @RolesAllowed({RoleType.Name.CONTRIBUTOR})
    public Question update(Question updatedQuestion) {
        Question oldQuestion = findById(updatedQuestion.getId());
        ImageUtil.validateImagePath(oldQuestion.getImage(), updatedQuestion.getImage());

        // The image was previously present and was just now removed.
        if (updatedQuestion.getImage() == null && oldQuestion.getImage() != null) {
            ImageUtil.deleteImage(oldQuestion.getImage());
        }

        em.merge(updatedQuestion);
        em.flush();
        logger.info("Question id: {} was UPDATED by user: {}", updatedQuestion.getId(), sessionContext.getCallerPrincipal().getName());

        return updatedQuestion;
    }

    @Override
    @RolesAllowed(RoleType.Name.MODERATOR)
    public void deleteById(Object id) {
        Question question = findById(id);

        if (question.getImage() != null) {
            ImageUtil.deleteImage(question.getImage());
        }

        em.remove(question);
        em.flush();
        logger.info("Question id: {} was DELETED by user: {}", id, sessionContext.getCallerPrincipal().getName());
    }

    @RolesAllowed(RoleType.Name.CONTRIBUTOR)
    public Question create(Question newQuestion, InputStream imageStream) {
        String imagePath = ImageUtil.saveImage(imageStream);
        newQuestion.setImage(imagePath);

        User user = userService.findByField(User_.name, sessionContext.getCallerPrincipal().getName());
        newQuestion.setUser(user);

        Question createdQuestion = super.create(newQuestion);
        logger.info("Question id: {} was CREATED by user id: {}", newQuestion.getId(), sessionContext.getCallerPrincipal().getName());
        return createdQuestion;
    }

    @Override
    @RolesAllowed(RoleType.Name.CONTRIBUTOR)
    public Question create(Question newQuestion) {
        User user = userService.findByField(User_.name, sessionContext.getCallerPrincipal().getName());
        newQuestion.setUser(user);
        newQuestion.setImage(null);

        Question createdQuestion = super.create(newQuestion);
        logger.info("Question id: {} was CREATED by user id: {}", newQuestion.getId(), sessionContext.getCallerPrincipal().getName());
        return createdQuestion;
    }

    @PermitAll
    public List<QuestionClient> toDto(List<Question> questions) {
        ModelMapper mapper = new ModelMapper();
        List<QuestionClient> questionsDto = mapper.map(questions, new TypeToken<List<QuestionClient>>() {
        }.getType());

        for (int i = 0; i < questionsDto.size(); i++) {
            String imagePath = new ArrayList<>(questions).get(i).getImage();
            if (imagePath != null) {
                questionsDto.get(i).setImageData(new ImageData(imagePath, ImageUtil.getDateCreated(imagePath)));
            }
        }

        return questionsDto;
    }
}