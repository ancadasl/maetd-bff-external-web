package es.maetd.marketcreators.bff.external.application.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Builder
public class ReportDTO {

    private String fileName;

    private String filePath;

    private String fileDate;

    private byte[] data;
}
