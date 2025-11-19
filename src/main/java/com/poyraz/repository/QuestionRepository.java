package com.poyraz.repository;


import com.poyraz.entity.Question;
import com.poyraz.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {


    List<Question> findByCategory(Category category);


}
