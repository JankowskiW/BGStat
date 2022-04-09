package pl.wj.bgstat.systemobjectattributeclass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClass;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClassId;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;

import java.util.List;

@Repository
public interface SystemObjectAttributeClassRepository extends JpaRepository<SystemObjectAttributeClass, SystemObjectAttributeClassId> {

    @Query("SELECT new pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto(" +
            "soac.id.attributeClassId, soac.id.systemObjectTypeId, soac.required, " +
            "soac.classDefaultValue, soac.attributeClass.name, soac.systemObjectType.name) " +
            "FROM SystemObjectAttributeClass soac LEFT JOIN soac.attributeClass ac LEFT JOIN soac.systemObjectType sot " +
            "WHERE soac.id.attributeClassId = :attributeClassId")
    List<SystemObjectAttributeClassResponseDto> findAllAssignmentsByAttributeClassId(long attributeClassId);

    @Query("SELECT new pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto(" +
            "soac.id.attributeClassId, soac.id.systemObjectTypeId, soac.required, " +
            "soac.classDefaultValue, soac.attributeClass.name, soac.systemObjectType.name) " +
            "FROM SystemObjectAttributeClass soac LEFT JOIN soac.attributeClass ac LEFT JOIN soac.systemObjectType sot " +
            "WHERE soac.id.systemObjectTypeId = :systemObjectTypeId")
    List<SystemObjectAttributeClassResponseDto> findAllAssignmentsBySystemObjectTypeId(long systemObjectTypeId);

    @Query("SELECT CASE WHEN COUNT(soac) > 0 THEN TRUE ELSE FALSE END " +
            "FROM SystemObjectAttributeClass soac WHERE soac.id.systemObjectTypeId = :id")
    boolean existsBySystemObjectTypeId(long id);
}
