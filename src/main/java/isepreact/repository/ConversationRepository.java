package isepreact.repository;

import org.springframework.data.repository.CrudRepository;
import isepreact.model.Conversation;

import java.util.List;
import java.util.Set;

/**
 * Created by victo on 13/05/2017.
 */
public interface ConversationRepository extends CrudRepository<Conversation, Integer> {

    public Conversation findById(Integer id);

    public Set<Conversation> findAll();

    public Set<Conversation> findByEleve_idOrderByDateDesc(Integer id);

    public Set<Conversation> findByProf_id(Integer id);

    public Set<Conversation> findByProf_idAndModuleConcerne_idOrderByDateDesc(Integer prof_id, Integer module_converne_id);

    public Set<Conversation> findByOffensifTrue();

    public List<Conversation> findByOffensifTrueAndOffensifViewedByAdminFalseOrderByDatePourNotif();

}
