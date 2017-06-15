package isepreact.repository;

import isepreact.model.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import isepreact.model.Questionnaire;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called questionnaireRepository
// CRUD refers Create, Read, Update, Delete

public interface QuestionnaireRepository extends CrudRepository<Questionnaire, Integer> {

  Questionnaire findById(Integer id);

  List<Questionnaire> findAllByOrderByDateDesc();

  List<Questionnaire> findAllByDeletedByAdminFalseOrderByDateDesc();

  List<Questionnaire> findByUser_idOrderByDateDesc(Integer id);

  @Query("SELECT q from Questionnaire q JOIN q.module m JOIN m.students ms WHERE ms.id = ?1 ORDER BY q.date DESC")
  List<Questionnaire> findWithStudentIdOrderByDateDesc(Integer id);

  List<Questionnaire> findAllByModule_id(Integer id);

  List<Questionnaire> findAllByModule_idOrderByDateDesc(Integer id);

}
