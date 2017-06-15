package isepreact.repository;


import isepreact.model.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called questionnaireRepository
// CRUD refers Create, Read, Update, Delete

public interface QuestionRepository extends CrudRepository<Question, Integer> {
    List<Question> findByQuestionnaire_id(Integer id);
    Question findById(Integer id);
}
