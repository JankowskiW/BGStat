package pl.wj.bgstat.attribute;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.attribute.model.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    boolean existsByObjectIdAndObjectTypeIdAndAttributeClassId(long objectId, long objectTypeId, long attributeClassId);
    boolean existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValueNot(
            long objectId, long objectTypeId, long attributeClassId, String value);
    boolean existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValueAndIdNot(
            long objectId, long objectTypeId, long attributeClassId, String value, long id);
}
