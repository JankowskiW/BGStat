package pl.wj.bgstat.attributeclass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.attributeclass.model.AttributeClass;

@Repository
public interface AttributeClassRepository extends JpaRepository<AttributeClass, Long> {
}
