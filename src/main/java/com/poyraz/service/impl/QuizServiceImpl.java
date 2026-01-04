package com.poyraz.service.impl;

import com.poyraz.dto.*;
import com.poyraz.entity.Answer;
import com.poyraz.entity.Question;
import com.poyraz.entity.Quiz;
import com.poyraz.entity.Submission;
import com.poyraz.enums.Category;
import com.poyraz.exceptions.CategoryNotExistException;
import com.poyraz.exceptions.NotEnoughQuestionsException;
import com.poyraz.exceptions.QuestionNotFoundException;
import com.poyraz.exceptions.QuizNotFoundException;
import com.poyraz.repository.QuestionRepository;
import com.poyraz.repository.QuizRepository;
import com.poyraz.repository.SubmissionRepository;
import com.poyraz.service.QuizService;
import com.poyraz.util.QuestionMapper;
import com.poyraz.util.QuizMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class QuizServiceImpl implements QuizService {

    @Value("${category.not.exist}")
    private String categoryNotExistMessage;
    @Value("${not.enough.questions}")
    private String notEnoughQuestionsMessage;
    @Value("${quiz.creation.message}")
    private String quizCreationMessage;
    @Value("${quiz.not.found}")
    private String quizNotFoundMessage;
    @Value("${quiz.not.match}")
    private String quizNotMatchMessage;
    @Value("${question.not.found}")
    private String questionNotFoundMessage;

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final SubmissionRepository submissionRepository;
    private final QuizMapper quizMapper;


    @Override
    public String createQuiz(String category, int noOfQuestions, String quizName) {
        Quiz quiz = Quiz.builder().quizName(quizName).category(Category.valueOf(category.toUpperCase()))
                .questionCount(noOfQuestions).createdAt(LocalDateTime.now()).build();
        List<QuestionDTO> list = createQuestionsOfTheQuiz(category, noOfQuestions);
        quiz.setQuestions(list.stream().map(questionMapper::questionDTOToQuestion).toList());
        quizRepository.save(quiz);
        return String.format(quizCreationMessage, quiz.getId());
    }

    @Override
    public List<QuestionWrapperDTO> getQuizQuestionsById(long id) throws QuizNotFoundException {
        if (quizRepository.existsById(id)) {
            Quiz quiz = quizRepository.findById(id).orElseThrow();
            return quiz.getQuestions().stream().map(questionMapper::questionToQuestionWrapperDTO).toList();
        } else {
            throw new QuizNotFoundException(String.format(quizNotFoundMessage, id));
        }
    }


    @Override
    public SubmissionResultDTO submitQuiz(long quizId, SubmissionDTO submissionDTO) throws QuizNotFoundException {
        if (!quizRepository.existsById(quizId)) {
            throw new QuizNotFoundException(String.format(quizNotFoundMessage, quizId));
        }

        List<Long> questionIds = quizRepository.findById(quizId).orElseThrow().getQuestions()
                .stream().map(Question::getId).toList();

        List<AnswerDTO> answerDTOS = submissionDTO.getAnswers();
        List<Long> answeredQuestionIds = answerDTOS.stream()
                .map(AnswerDTO::getQuestionId)
                .toList();

        Set<Long> questionIdSet = new HashSet<>(questionIds);
        Set<Long> answeredIdSet = new HashSet<>(answeredQuestionIds);
        if (!questionIdSet.equals(answeredIdSet)) {
            throw new QuizNotFoundException(String.format(quizNotMatchMessage, quizId));
        }

        SubmissionResultDTO resultDTO = calculateScores(answerDTOS);

        Submission submission = Submission.builder()
                .submitterName(submissionDTO.getSubmitterName())
                .quizId(quizId)
                .submittedAt(LocalDateTime.now())
                .build();


        List<Answer> entities = new ArrayList<>();
        for (AnswerDTO dto : answerDTOS) {
            Long qId = dto.getQuestionId();
            Question question = questionRepository.findById(qId)
                    .orElseThrow(() -> new QuestionNotFoundException(String.format(questionNotFoundMessage, qId)));

            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setUserAnswer(dto.getUserAnswer());
            answer.setSubmission(submission);
            entities.add(answer);
        }
        submission.setAnswers(entities);
        submissionRepository.save(submission);
        return resultDTO;
    }

    private SubmissionResultDTO calculateScores(List<AnswerDTO> answerDTOS) {
        int correct = 0;
        int wrong = 0;

        for (AnswerDTO dto : answerDTOS) {
            Long qId = dto.getQuestionId();

            Question question = questionRepository.findById(qId)
                    .orElseThrow(() -> new QuestionNotFoundException(String.format(questionNotFoundMessage, qId)));

            String submitted = dto.getUserAnswer() == null ? "" : dto.getUserAnswer().trim();
            String correctAnswer = question.getRightAnswer() == null ? "" : question.getRightAnswer().trim();

            if (submitted.equalsIgnoreCase(correctAnswer)) {
                correct++;
            } else {
                wrong++;
            }
        }

        int total = correct + wrong;
        double percentage = total == 0 ? 0.0 : (100.0 * correct) / total;

        return new SubmissionResultDTO(correct, wrong, total, percentage);
    }

    private List<QuestionDTO> createQuestionsOfTheQuiz(String category, int noOfQuestions) throws QuestionNotFoundException, NotEnoughQuestionsException {
        Category cat;
        try {
            cat = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CategoryNotExistException(String.format(categoryNotExistMessage, category));
        }
        List<QuestionDTO> list = new ArrayList<>(questionRepository.findByCategory(cat)
                .stream()
                .map(questionMapper::questionToQuestionDTO)
                .toList());

        if (list.size() < noOfQuestions) {
            throw new NotEnoughQuestionsException(String.format(notEnoughQuestionsMessage, category, noOfQuestions, list.size()));
        }
        Collections.shuffle(list);

        return list.subList(0, noOfQuestions);
    }


    @Override
    public Page<QuizDTO> getQuizzesPage(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(sortBy == null || sortBy.isBlank() ? "id" : sortBy);
        if ("desc".equalsIgnoreCase(direction)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Quiz> pageResult = quizRepository.findAll(pageRequest);
        return pageResult.map(quizMapper::quizToQuizDTO);
    }

    @Override
    public void deleteQuizById(long id) throws QuizNotFoundException {
        if (quizRepository.existsById(id)) {
            quizRepository.deleteById(id);
        } else {
            throw new QuizNotFoundException(String.format(quizNotFoundMessage, id));
        }
    }

}
