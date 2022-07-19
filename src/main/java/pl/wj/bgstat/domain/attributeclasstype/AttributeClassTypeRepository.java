package pl.wj.bgstat.domain.attributeclasstype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.domain.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.domain.attributeclasstype.model.dto.AttributeClassTypeHeaderDto;

import java.util.List;

@Repository
public interface AttributeClassTypeRepository extends JpaRepository<AttributeClassType, Long> {

    @Query("SELECT new pl.wj.bgstat.domain.attributeclasstype.model.dto.AttributeClassTypeHeaderDto(" +
            "act.id, act.name, act.archived, act.multivalued) " +
           "FROM AttributeClassType act")
    List<AttributeClassTypeHeaderDto> findAttributeClassTypeHeaders();
    @Query("SELECT new pl.wj.bgstat.domain.attributeclasstype.model.dto.AttributeClassTypeHeaderDto(" +
            "act.id, act.name, act.archived, act.multivalued) " +
            "FROM AttributeClassType act WHERE act.archived = :archived")
    List<AttributeClassTypeHeaderDto> findAttributeClassTypeHeaders(boolean archived);

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, long id);

    @Query("SELECT act.name FROM AttributeClassType act WHERE act.id = :id")
    String getNameById(long id);

    @Query("SELECT act.multivalued " +
            "FROM AttributeClass ac " +
            "LEFT JOIN ac.attributeClassType act " +
            "WHERE ac.id = :attributeClassId")
    boolean getMultivaluedStatusByAttributeClassId(long attributeClassId);
}
