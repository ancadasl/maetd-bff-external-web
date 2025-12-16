package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.web.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.exception.DocumentServiceException;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.DocumentDataResponseDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.DocumentDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetDocumentsIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.web.client.DocumentRepositoryRestAdapter;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.DocumentInfrastructureMapper;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.DocumentInfrastructureMapperImpl;
import es.maetd.marketcreators.clients.rest.domains.files.model.ExternalWebFileData;
import es.maetd.marketcreators.clients.rest.domains.files.model.FileData;
import es.maetd.marketcreators.clients.rest.domains.files.model.FileList;
import es.maetd.marketcreators.clients.rest.domains.files.model.FileResponse;
import es.maetd.marketcreators.clients.rest.service.base.RestClient;
import es.maetd.marketcreators.clients.rest.service.dto.input.RequestIDTO;
import es.maetd.marketcreators.ddd.core.events.domains.files.CategoryType;
import es.maetd.marketcreators.ddd.core.events.domains.files.OriginSystemType;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DocumentRepositoryRestAdapterTest {

    public static final String DESCRIPCION = "descripcion";

    public static final String CSB = "0049";

    @InjectMocks
    DocumentRepositoryRestAdapter adapter;

    @Mock
    RestClient restClient;

    @Mock
    DocumentInfrastructureMapper mapper;

    private static DocumentInfrastructureMapper mapperImpl;

    @BeforeAll
    static void setUp() {
        mapperImpl = new DocumentInfrastructureMapperImpl();
    }

    @Test
    void getFileClient_returnsOK() {
        // Given
        FileList list = new FileList();
        FileData file = createDummyDocumentFileData();
        list.setData(List.of(file));
        FileResponse fileResponse = new FileResponse();
        fileResponse.setFiles(list);
        fileResponse.setCountFiles(1);

        ResponseEntity<FileResponse> response = new ResponseEntity<>(fileResponse, HttpStatus.OK);

        // When
        when(restClient.execute(any(RequestIDTO.class))).thenReturn(response);
        when(mapper.map(any(FileList.class))).thenReturn(mapperImpl.map(list));

        GetDocumentsIDTO idtoMock = new GetDocumentsIDTO();

        idtoMock.setPage(0);
        idtoMock.setSize(15);
        idtoMock.setCategory(CategoryType.ACTAS_LETTER.name());

        DocumentDataResponseDto result = adapter.getDocuments(idtoMock);

        // Then
        Assertions.assertEquals(file.getFilename(), result.getList().get(0).getDocumentName());

        Assertions.assertEquals(result.getList().get(0).getDocumentName(), file.getFilename());

        verify(restClient, times(1)).execute(any(RequestIDTO.class));
        verify(mapper, times(1)).map(any(FileList.class));
    }

    @Test
    void getDocumentData_shouldReturnDocumentDto_whenResponseOK() {
        // Given
        UUID fileId = UUID.randomUUID();
        byte[] bytes = "hello".getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment().filename("fileTest.txt").build());

        ResponseEntity<byte[]> response = new ResponseEntity<>(bytes, headers, HttpStatus.OK);

        when(restClient.execute(any(RequestIDTO.class))).thenReturn(response);

        // when
        DocumentDto result = adapter.getDocumentData(fileId);

        // then
        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(bytes, result.getData());
        Assertions.assertEquals("fileTest.txt", result.getDocumentName());

        verify(restClient, times(1)).execute(any());
    }

    @Test
    void getDocumentData_shouldThrowException_whenResponseIsNot2xx() {
        // Given
        UUID fileId = UUID.randomUUID();

        ResponseEntity<byte[]> response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        when(restClient.execute(any(RequestIDTO.class))).thenReturn(response);

        // When
        Assertions.assertThrows(DocumentServiceException.class, () -> adapter.getDocumentData(fileId));
        verify(restClient, times(1)).execute(any());
    }

    private FileData createDummyDocumentFileData() {

        FileData file = new FileData();
        file.setId(UUID.randomUUID());
        file.setFilename("fileTest.txt");
        file.setExternalWebFileData(createExternalWebFileData());
        file.setOriginSystem(OriginSystemType.EXTERNAL_WEB.name());
        return file;
    }

    private static ExternalWebFileData createExternalWebFileData() {
        ExternalWebFileData data = new ExternalWebFileData();
        data.setActive(true);
        data.setCsb(CSB);
        data.setCategoryType(ExternalWebFileData.CategoryTypeEnum.ACTAS_BOND);
        data.setDescription(DESCRIPCION);
        return data;
    }
}
