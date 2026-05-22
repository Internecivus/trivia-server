package com.trivia.persistence;

public enum EntityView {
    QuestionDetails(Name.QUESTION_DETAILS),
    QuestionList(Name.QUESTION_LIST),
    UserDetails(Name.USER_DETAILS),
    ClientDetails(Name.CLIENT_DETAILS);

    private String name;

    EntityView(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static class Name {
        public final static String QUESTION_DETAILS = "graph.Question.details";
        public final static String QUESTION_LIST = "graph.Question.list";
        public final static String USER_DETAILS = "graph.User.details";
        public final static String CLIENT_DETAILS = "graph.Client.details";
    }
}




