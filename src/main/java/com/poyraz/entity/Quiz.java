package com.poyraz.entity;

import com.poyraz.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "quiz_name", nullable = false, unique = true)
    private String quizName;
    @Enumerated(value = EnumType.STRING)
    private Category category;
    @Column(name = "question_count")
    private int questionCount;
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(name = "quiz_questions",
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<Question> questions;
}
