package br.com.devdojo.examgenerator.persistence.repository;

import br.com.devdojo.examgenerator.persistence.model.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@NoRepositoryBean
public interface CustomPagingAndSortRepository<T extends AbstractEntity, ID extends Long> extends PagingAndSortingRepository<T, ID> {
    @Override
    @Query("select e from #{#entityName} e where e.professor = ?#{principal.professor} and e.enabled = true")
    Iterable<T> findAll(Sort sort);

    @Override
    @Query("select e from #{#entityName} e where e.professor = ?#{principal.professor} and e.enabled = true")
    Page<T> findAll(Pageable pageable);

    @Query("select e from #{#entityName} e where e.id=?1 and e.professor = ?#{principal.professor} and e.enabled = true")
    T findBy(Long id);

    @Query("select e from #{#entityName} e where e.id=?1 and e.professor = ?#{principal.professor} and e.enabled = true")
    T findBy(T t);

    @Override
    default boolean existsById(Long id){
        return findBy(id) != null;
    }

    @Override
    @Query("select e from #{#entityName} e where e.professor = ?#{principal.professor} and e.enabled = true")
    Iterable<T> findAll();

    @Override
    @Query("select e from #{#entityName} e where e.professor = ?#{principal.professor} and e.enabled = true")
    Iterable<T> findAllById(Iterable<ID> iterable);

    @Override
    @Query("select count(e) from #{#entityName} e where e.professor = ?#{principal.professor} and e.enabled = true")
    long count();

    @Override
    @Transactional
    @Modifying
    @Query("update #{#entityName}e set e.enabled=false where e.id=?1 and e.professor = ?#{principal.professor}")
    void deleteById(Long id);

    @Override
    default void delete(T t){
        deleteById(t.getId());
    }

    @Override
    @Transactional
    @Modifying
    default void deleteAll(Iterable<? extends T> iterable){
        iterable.forEach(entity -> deleteById(entity.getId()));
    }

    @Override
    @Transactional
    @Modifying
    @Query("update #{#entityName} e set e.enabled=false where e.professor = ?#{principal.professor}")
    void deleteAll();
}
