package isepreact.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import isepreact.model.Commentaire;

import java.util.Set;

/**
 * Created by victo on 13/05/2017.
 */
public interface CommentaireRepository extends CrudRepository<Commentaire, Integer>{

    public Set<Commentaire> findAllByOrderByDateDesc();

    public Set<Commentaire> findByDestinataire_idOrderByDateDesc(Integer id);

    public Set<Commentaire> findAllByOrderByDate();

    public Set<Commentaire> findByLuFalse();

    public Set<Commentaire> findByDestinataire_idAndLuFalse(Integer id);

    public Commentaire findById(Integer id);
}
