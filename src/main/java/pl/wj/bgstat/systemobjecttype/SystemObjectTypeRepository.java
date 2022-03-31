package pl.wj.bgstat.systemobjecttype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;

@Repository
public interface SystemObjectTypeRepository extends JpaRepository<SystemObjectType, Long> {

}
