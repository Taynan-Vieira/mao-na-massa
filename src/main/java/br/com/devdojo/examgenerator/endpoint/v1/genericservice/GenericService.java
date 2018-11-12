package br.com.devdojo.examgenerator.endpoint.v1.genericservice;

import br.com.devdojo.examgenerator.exception.ResourceNotFoundException;
import br.com.devdojo.examgenerator.persistence.model.AbstractEntity;
import br.com.devdojo.examgenerator.persistence.repository.CustomPagingAndSortRepository;
import org.springframework.stereotype.Service;

@Service
public class GenericService {

    public <T extends AbstractEntity, ID extends Long>
    void throwResourceNotFoundIfDoesNotExist(T t, CustomPagingAndSortRepository<T, ID> repository, String msg) {
        if (t == null || t.getId() == null || repository.
                 findBy(t.getId()) == null)
            throw new ResourceNotFoundException(msg);
    }

    public <T extends AbstractEntity, ID extends Long>
    void throwResourceNotFoundIfDoesNotExist(long id, CustomPagingAndSortRepository<T, ID> repository, String msg) {
        if (id == 0 || repository.findBy(id) == null)
            throw new ResourceNotFoundException(msg);
    }
}
