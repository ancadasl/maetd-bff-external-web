package es.maetd.marketcreators.bff.external.application.port.client;

import es.maetd.marketcreators.bff.external.application.service.dto.ReportDTO;

/**
 * Outbound Port responsible for report transfer through an SFTP
 * channel or equivalent remote service.
 *
 * <p>
 * This interface defines the contract that must be implemented by the
 * infrastructure layer responsible for retrieving the binary content
 * of a report from an external system based on its path.
 * </p>
 *
 * <p>
 * Part of a hexagonal architecture, allowing the application logic
 * to remain decoupled from technical details of communication with
 * external systems.
 * </p>
 */
public interface ReportTransferSftpRestPort {

    /**
     * Downloads a report from the external system using the provided path.
     *
     * @param path the full path of the file in the remote system.
     *             Must not be {@code null}.
     * @return {@link ReportDTO} containing the file name and its binary content.
     * @throws RuntimeException if an error occurs during the download
     *                          or if the file cannot be retrieved.
     */
    ReportDTO downloadReport(String path);
}
