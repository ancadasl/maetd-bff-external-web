package es.maetd.marketcreators.bff.external.infrastructure.adapters.sftp;

import es.maetd.marketcreators.bff.external.application.service.dto.ReportDTO;
import es.maetd.marketcreators.bff.external.exception.ReportTransferException;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.ReportInfrastructureMapper;
import es.maetd.marketcreators.sftp.client.configuration.props.FileTransferSFTPConfigurationProps;
import es.maetd.marketcreators.sftp.client.service.FileTransferService;
import es.maetd.marketcreators.sftp.client.service.dto.common.FileTransferCommonDTO;
import es.maetd.marketcreators.sftp.client.service.dto.input.DownloadFileServiceIDTO;
import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportTransferSftpService {

    private final FileTransferService fileTransferService;

    private final FileTransferSFTPConfigurationProps sftpProps;

    private final ReportInfrastructureMapper mapper;

    public List<ReportDTO> getReports() {

        List<ReportDTO> reportDTOList = Collections.emptyList();
        try {
            FileTransferCommonDTO dto = buildCommonDto();

            reportDTOList = mapper.map(fileTransferService.listDirectory(dto, sftpProps.getDefaultUploadPath()));

        } catch (Exception e) {
            log.error("Error listening files SFTP", e);
            throw new ReportTransferException("Error calling method list File in SFTP");
        }
        return reportDTOList;
    }

    public ReportDTO downloadReport(String fileName) {

        try {
            String nameWithoutExtension = fileName.replaceFirst("[.][^.]+$", "");
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            File tempFile =
                    Files.createTempFile(nameWithoutExtension, "." + extension).toFile();
            tempFile.deleteOnExit();

            FileSystemResource resource = new FileSystemResource(tempFile);

            DownloadFileServiceIDTO dto = DownloadFileServiceIDTO.builder()
                    .hostname(sftpProps.getHostname())
                    .port(sftpProps.getPort())
                    .username(sftpProps.getUsername())
                    .password(sftpProps.getPassword())
                    .originPath(sftpProps.getDefaultUploadPath() + "/" + fileName)
                    .destinationResource(resource)
                    .build();

            fileTransferService.downloadFile(dto);

            byte[] data = Files.readAllBytes(tempFile.toPath());

            return ReportDTO.builder().data(data).name(fileName).build();

        } catch (Exception e) {
            throw new ReportTransferException("Error downloading report from SFTP");
        }
    }

    private FileTransferCommonDTO buildCommonDto() {
        FileTransferCommonDTO dto = new FileTransferCommonDTO();
        dto.setHostname(sftpProps.getHostname());
        dto.setPort(sftpProps.getPort());
        dto.setUsername(sftpProps.getUsername());
        dto.setPassword(sftpProps.getPassword());
        return dto;
    }
}
