package pl.wj.bgstat.systemobjecttype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;

import java.util.List;

@Repository
public interface SystemObjectTypeRepository extends JpaRepository<SystemObjectType, Long> {

    @Query("SELECT new pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeHeaderDto(sot.id, sot.name, sot.archived) " +
           "FROM SystemObjectType sot")
    List<SystemObjectTypeHeaderDto> findAllSystemObjectTypeHeaders();

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String anyString, long anyLong);
}
