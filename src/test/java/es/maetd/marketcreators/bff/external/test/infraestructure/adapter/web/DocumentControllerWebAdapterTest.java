package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.mappers.DocumentApplicationMapper;
import es.maetd.marketcreators.bff.external.application.mappers.DocumentApplicationMapperImpl;
import es.maetd.marketcreators.bff.external.application.service.DocumentApplicationService;
import es.maetd.marketcreators.bff.external.application.service.dto.DocumentDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.DocumentDataResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetDocumentsIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.web.DocumentControllerWebAdapter;
import es.maetd.marketcreators.ddd.core.events.domains.files.CategoryType;
import es.maetd.marketcreators.ddd.core.events.domains.files.FileType;
import es.maetd.marketcreators.ddd.core.events.domains.files.OriginSystemType;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.DocumentDataListWDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DocumentControllerWebAdapterTest {

    @Mock
    private DocumentApplicationService service;

    @Mock
    private DocumentApplicationMapper mapper;

    @InjectMocks
    private DocumentControllerWebAdapter controller;

    private static DocumentApplicationMapper mapperImpl;

    @BeforeAll
    static void setUp() {
        mapperImpl = new DocumentApplicationMapperImpl();
    }

    @Test
    void whenGetDocumentListIsCalled_thenResponseShouldBeOk() {
        // given
        Optional<Integer> page = Optional.of(0);
        Optional<Integer> size = Optional.of(15);
        Optional<LocalDate> creationDate = Optional.of(LocalDate.now());
        Optional<String> category = Optional.of(CategoryType.ACTAS_LETTER.name());
        Optional<String> csb = Optional.of("csb");
        Optional<String> language = Optional.of("ES");

        DocumentDTO dto = createDummyDocumentDto();
        DocumentDataResponseDTO resDocument =
                DocumentDataResponseDTO.builder().list(List.of(dto)).build();

        // when
        when(service.getFiles(any(GetDocumentsIDTO.class))).thenReturn(resDocument);
        when(mapper.map(anyList())).thenReturn(mapperImpl.map(resDocument.getList()));

        // then
        ResponseEntity<DocumentDataListWDTO> response =
                controller.getDocuments(page, size, creationDate, category, csb, language);

        Assertions.assertEquals(
                resDocument.getList().get(0).getDocumentName(),
                Objects.requireNonNull(response.getBody()).getDocuments().get(0).getDocumentName());

        verify(service, times(1)).getFiles(any(GetDocumentsIDTO.class));
        verify(mapper, times(1)).map(anyList());
    }

    @Test
    void testGetDocumentData() {
        // Given
        UUID id = UUID.randomUUID();
        byte[] data = "contenido-de-prueba".getBytes();

        DocumentDTO dto = createDummyDocumentDto();

        dto.setData(data);

        when(service.getDocumentData(id)).thenReturn(dto);

        // When
        ResponseEntity<Resource> response = controller.getDocumentData(id);

        // Then
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());

        Assertions.assertEquals(
                "attachment; filename=\"fileTest.txt\"",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));

        Assertions.assertEquals(data.length, response.getHeaders().getContentLength());
        Assertions.assertEquals(
                MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());

        Assertions.assertNotNull(response.getBody());
        Assertions.assertDoesNotThrow(
                () -> Assertions.assertEquals(data.length, response.getBody().contentLength()));

        verify(service, times(1)).getDocumentData(id);
    }

    private DocumentDTO createDummyDocumentDto() {
        return DocumentDTO.builder()
                .data(new byte[0])
                .documentName("fileTest.txt")
                .fileType(FileType.EXTERNAL_CREATOR_FILE)
                .originSystem(OriginSystemType.BDE)
                .description("Descripción documento")
                .build();
    }
}
