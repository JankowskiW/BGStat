package pl.wj.bgstat.attribute;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attribute.model.dto.AttributeRequestDto;
import pl.wj.bgstat.attribute.model.dto.AttributeResponseDto;

@Service
@RequiredArgsConstructor
public class AttributeService {

    private final AttributeRepository attributeRepository;

    public AttributeResponseDto addAttribute(AttributeRequestDto attributeRequestDto) {
        // TODO: 25.04.2022 Make transaction with right lock method, when new multivalued attribute is created,
        //  other user shouldn't have possibility to create or delete attributes of this class on this object
        throw new NotYetImplementedException();
    }

    public long getLastOrdinalNumber(long anyLong, long anyLong1, long anyLong2) {
        // TODO: 25.04.2022 Is ordinal number even necessary?
        return 0;
    }
}
