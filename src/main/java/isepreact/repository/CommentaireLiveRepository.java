package isepreact.repository;

import isepreact.model.CommentaireLive;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Created by victo on 03/06/2017.
 */
public interface CommentaireLiveRepository extends CrudRepository<CommentaireLive, Integer> {

    public Set<CommentaireLive> findByQuestionnaire_id(Integer id);

    public Set<CommentaireLive> findByQuestionnaire_idAndUser_id(Integer questionnaire_id, Integer user_id);
}
