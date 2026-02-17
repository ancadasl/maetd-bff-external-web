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

    private String name;

    private String path;

    private String extension;

    private byte[] data;
}
