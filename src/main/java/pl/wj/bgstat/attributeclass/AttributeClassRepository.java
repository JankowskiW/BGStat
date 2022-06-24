package pl.wj.bgstat.attributeclass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassHeaderDto;

import java.util.Optional;

@Repository
public interface AttributeClassRepository extends JpaRepository<AttributeClass, Long> {

    @Query("SELECT new pl.wj.bgstat.attributeclass.model.dto.AttributeClassHeaderDto(ac.id, ac.name) FROM AttributeClass ac")
    Page<AttributeClassHeaderDto> findAllAttributeClassHeaders(Pageable pageable);

    @Query("SELECT ac FROM AttributeClass ac LEFT JOIN FETCH ac.attributeClassType act WHERE ac.id = :id")
    Optional<AttributeClass> findWithAttributeClassTypeById(long id);

    boolean existsByNameAndIdNot(String name, long id);
    boolean existsByName(String name);
    boolean existsByAttributeClassTypeId(long attributeClassTypeId);

}
