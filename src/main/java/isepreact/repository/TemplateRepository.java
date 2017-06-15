package isepreact.repository;

import isepreact.model.Template;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Created by victo on 16/05/2017.
 */
public interface TemplateRepository extends CrudRepository<Template, Integer> {

    public Set<Template> findAll();

    public Template findById(Integer id);

    public Set<Template> findByUser_id(Integer id);

    public Template findByQuestionnaire_id(Integer id);

}
