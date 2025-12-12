package com.poyraz.service;

import com.poyraz.dto.QuestionWrapperDTO;
import com.poyraz.dto.SubmissionDTO;
import com.poyraz.dto.SubmissionResultDTO;
import com.poyraz.exceptions.NotEnoughQuestionsException;
import com.poyraz.exceptions.QuestionNotFoundException;
import com.poyraz.exceptions.QuizNotFoundException;

import java.util.List;

public interface QuizService {
    String createQuiz(String category, int noOfQuestions, String quizName) throws QuestionNotFoundException, NotEnoughQuestionsException;

    List<QuestionWrapperDTO> getQuizQuestionsById(long id) throws QuizNotFoundException;


    SubmissionResultDTO submitQuiz(long quizId, SubmissionDTO submissionDTO) throws QuizNotFoundException;

}
