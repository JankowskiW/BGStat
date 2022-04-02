package pl.wj.bgstat.attributeclasstype;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto;

@Repository
public interface AttributeClassTypeRepository extends JpaRepository<AttributeClassType, Long> {

    @Query("SELECT new pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto(act.id, act.name, act.archived) " +
           "FROM AttributeClassType act")
    Page<AttributeClassTypeHeaderDto> findAllAttributeClassTypeHeaders(Pageable pageable);

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, long id);
}
