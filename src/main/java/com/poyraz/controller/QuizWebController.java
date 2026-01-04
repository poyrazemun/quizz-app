package com.poyraz.controller;

import com.poyraz.dto.*;
import com.poyraz.enums.Category;
import com.poyraz.exceptions.QuizNotFoundException;
import com.poyraz.service.QuizService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ui")
@RequiredArgsConstructor
public class QuizWebController {
    private final QuizService quizService;

    @GetMapping("/quiz/create")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("quizForm")) {
            model.addAttribute("quizForm", new QuizFormDTO());
        }
        model.addAttribute("categories", Category.values());
        return "quiz/form";
    }

    @PostMapping("/quiz/create")
    public String submitCreateForm(@Valid @ModelAttribute("quizForm") QuizFormDTO quizFormDTO,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", Category.values());
            return "quiz/form";
        }

        quizService.createQuiz(quizFormDTO.getCategory(), quizFormDTO.getNoOfQuestions(), quizFormDTO.getQuizName());
        return "redirect:/ui/questions";
    }

    @GetMapping("/quizzes")
    public String listQuizzes(Model model,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                              @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        int pageSize = 10;
        Page<?> quizzesPage = quizService.getQuizzesPage(page, pageSize, sortBy, direction);

        model.addAttribute("quizzes", quizzesPage.getContent());
        model.addAttribute("currentPage", quizzesPage.getNumber());
        model.addAttribute("totalPages", quizzesPage.getTotalPages());
        model.addAttribute("hasPrevious", quizzesPage.hasPrevious());
        model.addAttribute("hasNext", quizzesPage.hasNext());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "quiz/list";
    }

    @GetMapping("/quiz/{id}/delete")
    public String deleteQuiz(@PathVariable("id") long id) {
        try {
            quizService.deleteQuizById(id);
        } catch (QuizNotFoundException e) {
            // I should log the exception here
        }
        return "redirect:/ui/quizzes";
    }

    @GetMapping("/quiz/{id}/view")
    public String viewQuiz(@PathVariable("id") long id, Model model) {
        List<QuestionWrapperDTO> questions = quizService.getQuizQuestionsById(id);
        model.addAttribute("questions", questions);
        model.addAttribute("quizId", id);
        return "quiz/detail";
    }

    @PostMapping("/quiz/{id}/submit")
    public String submitQuiz(@PathVariable("id") long id,
                             HttpServletRequest request,
                             Model model) {
        String submitterName = request.getParameter("submitterName");
        Map<String, String[]> params = request.getParameterMap();
        List<AnswerDTO> answers = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("answer_")) {
                try {
                    String qIdStr = key.substring("answer_".length());
                    Long qId = Long.parseLong(qIdStr);
                    String[] values = entry.getValue();
                    String userAnswer = (values != null && values.length > 0) ? values[0] : "";
                    AnswerDTO answerDTO = new AnswerDTO();
                    answerDTO.setQuestionId(qId);
                    answerDTO.setUserAnswer(userAnswer);
                    answers.add(answerDTO);
                } catch (NumberFormatException e) {
                    // ignore invalid param
                }
            }
        }

        SubmissionDTO submissionDTO = new SubmissionDTO();
        submissionDTO.setSubmitterName(submitterName);
        submissionDTO.setAnswers(answers);

        SubmissionResultDTO result = quizService.submitQuiz(id, submissionDTO);

        model.addAttribute("result", result);
        model.addAttribute("quizId", id);
        return "quiz/result";
    }
}
