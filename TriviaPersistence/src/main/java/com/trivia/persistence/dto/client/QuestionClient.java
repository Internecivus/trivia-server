package com.trivia.persistence.dto.client;


public class QuestionClient {
    private int id;
    private String question;
    private String answerFirst;
    private String answerSecond;
    private String answerThird;
    private String answerFourth;
    private int answerCorrect;
    private ImageData imageData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnswerCorrect() {
        return answerCorrect;
    }

    public void setAnswerCorrect(int answerCorrect) {
        this.answerCorrect = answerCorrect;
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

    public ImageData getImageData() {
        return imageData;
    }

    public void setImageData(ImageData imageData) {
        this.imageData = imageData;
    }
}
