package dc.icdc.workflow.helper;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

@NoRepositoryBean
public interface CustomRepository<T, ID extends Serializable>
        extends CrudRepository<T, ID> {
    void refresh(T t);
}