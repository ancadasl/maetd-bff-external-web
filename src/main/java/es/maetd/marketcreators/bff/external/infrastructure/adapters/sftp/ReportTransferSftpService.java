package es.maetd.marketcreators.bff.external.infrastructure.adapters.sftp;

import es.maetd.marketcreators.bff.external.application.service.dto.ReportDTO;
import es.maetd.marketcreators.bff.external.exception.ReportTransferException;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.ReportInfrastructureMapper;
import es.maetd.marketcreators.sftp.client.configuration.props.FileTransferSFTPConfigurationProps;
import es.maetd.marketcreators.sftp.client.service.FileTransferService;
import es.maetd.marketcreators.sftp.client.service.dto.input.DownloadFileServiceIDTO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public ReportDTO downloadReport(String path) {

        try {

            Path filePath = Paths.get(path);

            String fileName = filePath.getFileName().toString(); // informe.final.v2.2026.pdf

            String nameWithoutExtension = fileName;
            String extension = "";

            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                nameWithoutExtension = fileName.substring(0, dotIndex);
                extension = fileName.substring(dotIndex + 1);
            }
            File tempFile =
                    Files.createTempFile(nameWithoutExtension, "." + extension).toFile();
            tempFile.deleteOnExit();

            FileSystemResource resource = new FileSystemResource(tempFile);

            DownloadFileServiceIDTO dto = DownloadFileServiceIDTO.builder()
                    .hostname(sftpProps.getHostname())
                    .port(sftpProps.getPort())
                    .username(sftpProps.getUsername())
                    .password(sftpProps.getPassword())
                    .originPath(path)
                    .destinationResource(resource)
                    .build();

            fileTransferService.downloadFile(dto);

            byte[] data = Files.readAllBytes(tempFile.toPath());

            return ReportDTO.builder().data(data).fileName(fileName).build();

        } catch (Exception e) {
            throw new ReportTransferException("Error downloading report from SFTP");
        }
    }
}
