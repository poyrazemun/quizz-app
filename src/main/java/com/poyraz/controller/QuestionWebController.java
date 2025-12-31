package com.poyraz.controller;

import com.poyraz.dto.QuestionDTO;
import com.poyraz.enums.Category;
import com.poyraz.enums.DifficultyLevel;
import com.poyraz.exceptions.QuestionNotFoundException;
import com.poyraz.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui")
@RequiredArgsConstructor
public class QuestionWebController {

    private final QuestionService questionService;

    @GetMapping("/questions")
    public String listQuestions(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        int pageSize = 10;
        Page<QuestionDTO> questionsPage = questionService.getQuestionsPage(page, pageSize);

        model.addAttribute("questions", questionsPage.getContent());
        model.addAttribute("currentPage", questionsPage.getNumber());
        model.addAttribute("totalPages", questionsPage.getTotalPages());
        model.addAttribute("hasPrevious", questionsPage.hasPrevious());
        model.addAttribute("hasNext", questionsPage.hasNext());
        return "question/list";
    }

    @GetMapping("/question/{id}/view")
    public String viewQuestion(@PathVariable("id") long id, Model model) {
        QuestionDTO question = questionService.getQuestionById(id);
        model.addAttribute("question", question);
        return "question/detail";
    }

    @GetMapping("/question/edit")
    public String showEditForm(@RequestParam("id") long id, Model model) {
        QuestionDTO questionDTO = questionService.getQuestionById(id);
        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("categories", Category.values());
        model.addAttribute("difficultyLevels", DifficultyLevel.values());
        return "question/form";
    }

    @GetMapping("/question/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("questionDTO")) {
            model.addAttribute("questionDTO", new QuestionDTO());
        }
        model.addAttribute("categories", Category.values());
        model.addAttribute("difficultyLevels", DifficultyLevel.values());
        return "question/form";
    }

    @PostMapping("/question/add")
    public String submitAddForm(@Valid @ModelAttribute("questionDTO") QuestionDTO questionDTO,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", Category.values());
            model.addAttribute("difficultyLevels", DifficultyLevel.values());
            return "question/form";
        }

        questionService.addQuestion(questionDTO);
        return "redirect:/ui/questions";
    }

    @GetMapping("/question/random")
    public String randomQuestion(Model model) {
        // delegate to service. If service needs an id param, pass 1 as in REST controller
        QuestionDTO question = questionService.getRandomQuestion(1L);
        model.addAttribute("question", question);
        return "question/random";
    }

    @GetMapping("/question/{id}/delete")
    public String deleteQuestion(@PathVariable("id") long id) {
        try {
            questionService.deleteQuestionById(id);
        } catch (QuestionNotFoundException e) {
            // Could add a flash message; for simplicity, ignore and continue
        }
        return "redirect:/ui/questions";
    }
}
