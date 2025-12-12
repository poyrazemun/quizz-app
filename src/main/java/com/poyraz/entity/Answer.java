package com.poyraz.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class Answer {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "user_answer", nullable = false)
    private String userAnswer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    @ToString.Exclude
    private Question question;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    @ToString.Exclude
    private Submission submission;
}
