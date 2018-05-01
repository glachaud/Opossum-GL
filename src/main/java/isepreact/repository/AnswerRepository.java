package isepreact.repository;

import isepreact.model.Questionnaire;
import org.springframework.data.repository.CrudRepository;

import isepreact.model.Answer;

import java.util.Set;

// This will be AUTO IMPLEMENTED by Spring into a Bean called questionnaireRepository
// CRUD refers Create, Read, Update, Delete

public interface AnswerRepository extends CrudRepository<Answer, Integer> {

    Set<Answer> findByUser_idAndQuestionnaire_id(Integer student_id, Integer questionnaire_id);

    Set<Answer> findByQuestionnaire_id(Integer questionnaire_id);
}
