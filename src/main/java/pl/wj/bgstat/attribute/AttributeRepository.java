package pl.wj.bgstat.attribute;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.attribute.model.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
}
