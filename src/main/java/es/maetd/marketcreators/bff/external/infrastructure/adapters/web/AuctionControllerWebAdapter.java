package es.maetd.marketcreators.bff.external.infrastructure.adapters.web;

import es.maetd.marketcreators.bff.external.application.mappers.AuctionApplicationMapper;
import es.maetd.marketcreators.bff.external.application.service.AuctionApplicationService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.*;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.AuctionsApi;
import org.openapitools.model.AuctionsDataGroupWDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuctionControllerWebAdapter extends BaseController implements AuctionsApi {

    private final AuctionApplicationService service;

    private final AuctionApplicationMapper mapper;

    public AuctionControllerWebAdapter(AuctionApplicationService service, AuctionApplicationMapper mapper) {

        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<AuctionsDataGroupWDTO> getAuctions(
            Optional<String> productType, Optional<String> csb, Optional<Integer> month, Optional<Integer> year) {
        log.info(
                "Get auctions for productType: {} , csb: {} , year: {}, month: {}.",
                productType.orElse(null),
                csb.orElse(null),
                year.orElse(null),
                month.orElse(null));

        doValidateCsbPermission(csb.orElse(null));

        GetAuctionsIDTO idto = GetAuctionsIDTO.builder()
                .productType(productType.map(ProductType::fromString).orElse(null))
                .csb(csb.orElse(null))
                .year(year.orElse(null))
                .month(month.orElse(null))
                .build();

        AuctionResponseDTO dto = service.getAuctions(idto);

        AuctionsDataGroupWDTO response = new AuctionsDataGroupWDTO();

        response.setPrimaryAuctions(mapper.map(dto.getPrimary()));

        response.setSecondaryAuctions(mapper.map(dto.getSecondary()));

        return ResponseEntity.ok(response);
    }
}
