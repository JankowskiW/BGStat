package pl.wj.bgstat.systemobjectattributeclass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClass;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClassId;

@Repository
public interface SystemObjectAttributeClassRepository extends JpaRepository<SystemObjectAttributeClass, SystemObjectAttributeClassId> {

    boolean existsByAttributeClassIdAndSystemObjectTypeId(long attributeClassId, long systemObjectTypeId);
}
