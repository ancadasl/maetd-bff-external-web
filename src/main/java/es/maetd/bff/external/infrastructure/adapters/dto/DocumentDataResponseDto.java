package es.maetd.bff.external.infrastructure.adapters.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class DocumentDataResponseDto {

    private List<DocumentDto> documentDtoList;

    private Integer count;
}
