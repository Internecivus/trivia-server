package com.trivia.persistence.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trivia.persistence.EntityView;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;



@Entity
@NamedEntityGraph(
    name = EntityView.Name.QUESTION_DETAILS,
    attributeNodes = {
        @NamedAttributeNode(value = "categories"),
        @NamedAttributeNode(value = "user")
    }
)
@NamedEntityGraph(
    name = EntityView.Name.QUESTION_LIST,
    attributeNodes = {
        @NamedAttributeNode(value = "user")
    }
)
@Table(name = "question", schema = "Trivia")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "question")
    @NotBlank(message = "{field.required}")
    @Size(max = 256, message = "{field.lengthMax}")
    private String question;

    @Basic
    @Column(name = "answer_first")
    @NotBlank(message = "{field.required}")
    @Size(max = 64, message = "{field.lengthMax}")
    private String answerFirst;

    @Basic
    @Column(name = "answer_second")
    @NotBlank(message = "{field.required}")
    @Size(max = 64, message = "{field.lengthMax}")
    private String answerSecond;

    @Basic
    @Column(name = "answer_third")
    @NotBlank(message = "{field.required}")
    @Size(max = 64, message = "{field.lengthMax}")
    private String answerThird;

    @Basic
    @Column(name = "answer_fourth")
    @NotBlank(message = "{field.required}")
    @Size(max = 64, message = "{field.lengthMax}")
    private String answerFourth;

    @Basic
    @Column(name = "comment")
    @Size(max = 1024, message = "{field.lengthMax}")
    private String comment;

    @Basic
    @NotNull
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "answer_correct")
    @NotNull(message = "{field.required}")
    @Min(value = 1, message = "{field.valueMin}")
    @Max(value = 4, message = "{field.valueMax}")
    private Integer answerCorrect;

    @Basic
    @Column(name = "date_last_modified")
    private Timestamp dateLastModified;

    @Basic
    @Column(name = "image")
    @Size(max = 28, message = "{field.lengthMax}")
    private String image;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private User user;

    @JsonIgnore
    @NotEmpty(message = "{collection.required}")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="question_category_map",
        joinColumns = {@JoinColumn(name = "question_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")}
    )
    private Set<Category> categories = new HashSet<>();

    @PrePersist
    public void preCreate() {
        this.dateCreated = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    public void preUpdate() {
        this.dateLastModified = new Timestamp(System.currentTimeMillis());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerFirst() {
        return answerFirst;
    }

    public void setAnswerFirst(String answerFirst) {
        this.answerFirst = answerFirst;
    }

    public String getAnswerSecond() {
        return answerSecond;
    }

    public void setAnswerSecond(String answerSecond) {
        this.answerSecond = answerSecond;
    }

    public String getAnswerThird() {
        return answerThird;
    }

    public void setAnswerThird(String answerThird) {
        this.answerThird = answerThird;
    }

    public String getAnswerFourth() {
        return answerFourth;
    }

    public void setAnswerFourth(String answerFourth) {
        this.answerFourth = answerFourth;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = new Timestamp(dateCreated.getTime());
    }

    public Integer getAnswerCorrect() {
        return answerCorrect;
    }

    public void setAnswerCorrect(Integer answerCorrect) {
        this.answerCorrect = answerCorrect;
    }

    public Date getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(Date dateLastModified) {
        this.dateLastModified = new Timestamp(dateLastModified.getTime());
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return Objects.equals(id, question1.id) &&
            Objects.equals(question, question1.question) &&
            Objects.equals(answerFirst, question1.answerFirst) &&
            Objects.equals(answerSecond, question1.answerSecond) &&
            Objects.equals(answerThird, question1.answerThird) &&
            Objects.equals(answerFourth, question1.answerFourth) &&
            Objects.equals(comment, question1.comment) &&
            Objects.equals(dateCreated, question1.dateCreated) &&
            Objects.equals(answerCorrect, question1.answerCorrect) &&
            Objects.equals(dateLastModified, question1.dateLastModified) &&
            Objects.equals(image, question1.image) &&
            Objects.equals(user, question1.user) &&
            Objects.equals(categories, question1.categories);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, question, answerFirst, answerSecond, answerThird, answerFourth, comment, dateCreated, answerCorrect, dateLastModified, image, user, categories);
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", question='" + question + '\'' +
            ", answerFirst='" + answerFirst + '\'' +
            ", answerSecond='" + answerSecond + '\'' +
            ", answerThird='" + answerThird + '\'' +
            ", answerFourth='" + answerFourth + '\'' +
            ", comment='" + comment + '\'' +
            ", dateCreated=" + dateCreated +
            ", answerCorrect=" + answerCorrect +
            ", dateLastModified=" + dateLastModified +
            ", image='" + image + '\'' +
            ", user=" + user +
            ", categories=" + categories +
            '}';
    }
}