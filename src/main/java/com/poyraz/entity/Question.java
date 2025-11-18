package com.poyraz.entity;

import com.poyraz.enums.Category;
import com.poyraz.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Question {
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "question_text", nullable = false, unique = true)
    private String questionText;
    @Column(nullable = false)
    private String option1;
    @Column(nullable = false)
    private String option2;
    @Column(nullable = false)
    private String option3;
    private String option4;
    @Column(name = "right_answer", nullable = false)
    private String rightAnswer;
    @Enumerated(value = EnumType.STRING)
    private DifficultyLevel difficultyLevel;
    @Enumerated(value = EnumType.STRING)
    private Category category;
}
