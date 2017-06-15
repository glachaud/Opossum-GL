package isepreact.repository;

import isepreact.model.Folder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by chrom on 23/05/2017.
 */
public interface FolderRepository extends CrudRepository<Folder, Integer> {

    List<Folder> findAllByModule_id(Integer id);

    Folder findByName(String name);

    Folder findById(Integer id);

    Set<Folder> findByModule_idAndTeacher_id(Integer module_id, Integer teacher_id);

    Set<Folder> findByModule_idAndTeacher_idAndType(Integer module_id, Integer teacher_id, String type);

}
