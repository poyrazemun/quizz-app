package com.poyraz.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Answer {
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "user_answer", nullable = false)
    private String userAnswer;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // Link this answer to a Submission (which stores submitterName and quizId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;
}
