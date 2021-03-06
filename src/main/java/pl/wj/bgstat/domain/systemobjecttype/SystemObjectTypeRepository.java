package pl.wj.bgstat.domain.systemobjecttype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.domain.systemobjecttype.model.SystemObjectType;
import pl.wj.bgstat.domain.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;

import java.util.List;

@Repository
public interface SystemObjectTypeRepository extends JpaRepository<SystemObjectType, Long> {

    @Query("SELECT new pl.wj.bgstat.domain.systemobjecttype.model.dto.SystemObjectTypeHeaderDto(sot.id, sot.name, sot.archived) " +
           "FROM SystemObjectType sot")
    List<SystemObjectTypeHeaderDto> findSystemObjectTypeHeaders();

    @Query("SELECT new pl.wj.bgstat.domain.systemobjecttype.model.dto.SystemObjectTypeHeaderDto(sot.id, sot.name, sot.archived) " +
            "FROM SystemObjectType sot WHERE sot.archived = :archived")
    List<SystemObjectTypeHeaderDto> findSystemObjectTypeHeaders(boolean archived);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String anyString, long anyLong);
}
