package isepreact.repository;

import org.springframework.data.repository.CrudRepository;

import isepreact.model.Module;

import java.util.List;
import java.util.Set;

// This will be AUTO IMPLEMENTED by Spring into a Bean called questionnaireRepository
// CRUD refers Create, Read, Update, Delete

public interface ModuleRepository extends CrudRepository<Module, Integer> {

    Module findById(Integer id);

    public Set<Module> findByTeacher_id(Integer id);

    public Set<Module> findByStudents_id(Integer id);

    List<Module> findByViewedByAdminFalseOrderByDatePourNotif();

    List<Module> findByDatePourNotifIsNotNullOrderByDatePourNotifDesc();

}
