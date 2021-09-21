package com.example.iquiz;

public class QuizTimingModel {

    Long QuizStartTime, QuizEndTime;
    int QuizDuration;

    public QuizTimingModel()
    {

    }

    public QuizTimingModel(Long quizStartTime, Long quizEndTime, int quizDuration) {
        QuizStartTime = quizStartTime;
        QuizEndTime = quizEndTime;
        QuizDuration = quizDuration;

    }

    public Long getQuizStartTime() {
        return QuizStartTime;
    }

    public void setQuizStartTime(Long quizStartTime) {
        QuizStartTime = quizStartTime;
    }

    public Long getQuizEndTime() {
        return QuizEndTime;
    }

    public void setQuizEndTime(Long quizEndTime) {
        QuizEndTime = quizEndTime;
    }

    public int getQuizDuration() {
        return QuizDuration;
    }

    public void setQuizDuration(int quizDuration) {
        QuizDuration = quizDuration;
    }
}
