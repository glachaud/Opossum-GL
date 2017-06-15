package isepreact.repository;

import org.springframework.data.repository.CrudRepository;

import isepreact.model.Answer;

// This will be AUTO IMPLEMENTED by Spring into a Bean called questionnaireRepository
// CRUD refers Create, Read, Update, Delete

public interface AnswerRepository extends CrudRepository<Answer, Integer> {

}
