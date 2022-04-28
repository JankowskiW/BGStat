package pl.wj.bgstat.attributeclasstype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto;

import java.util.List;

@Repository
public interface AttributeClassTypeRepository extends JpaRepository<AttributeClassType, Long> {

    @Query("SELECT new pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto(act.id, act.name, act.archived) " +
           "FROM AttributeClassType act")
    List<AttributeClassTypeHeaderDto> findAttributeClassTypeHeaders();
    @Query("SELECT new pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto(act.id, act.name, act.archived) " +
            "FROM AttributeClassType act WHERE act.archived = :archived")
    List<AttributeClassTypeHeaderDto> findAttributeClassTypeHeaders(boolean archived);

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, long id);
}
