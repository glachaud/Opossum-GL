package isepreact.repository;

import org.springframework.data.repository.CrudRepository;

import isepreact.model.QuestionnaireAnswer;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called questionnaireRepository
// CRUD refers Create, Read, Update, Delete

public interface QuestionnaireAnswerRepository extends CrudRepository<QuestionnaireAnswer, Integer> {

}
