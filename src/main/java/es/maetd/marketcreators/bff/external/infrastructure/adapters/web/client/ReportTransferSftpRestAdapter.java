package es.maetd.marketcreators.bff.external.infrastructure.adapters.web.client;

import es.maetd.marketcreators.bff.external.application.port.client.ReportTransferSftpRestPort;
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
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportTransferSftpRestAdapter implements ReportTransferSftpRestPort {

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
            File tempFile = null;
            if (SystemUtils.IS_OS_UNIX) {
                FileAttribute<Set<PosixFilePermission>> attr =
                        PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"));
                tempFile = Files.createTempFile(nameWithoutExtension, "." + extension, attr)
                        .toFile(); // Compliant
            } else {
                tempFile = Files.createTempFile(nameWithoutExtension, "." + extension)
                        .toFile(); // Compliant
                tempFile.setReadable(true, true);
                tempFile.setWritable(true, true);
                tempFile.setExecutable(true, true);
            }
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
