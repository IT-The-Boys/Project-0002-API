package it.theboys.project0002api.controller;


import it.theboys.project0002api.dto.database.QueryWithPageDTO;
import it.theboys.project0002api.dto.http.response.PageResponseDTO;
import it.theboys.project0002api.enums.GameName;
import it.theboys.project0002api.exception.database.BadRequestException;
import it.theboys.project0002api.model.database.CardSet;
import it.theboys.project0002api.model.database.cah.CahCard;
import it.theboys.project0002api.service.cardgame.CardService;
import it.theboys.project0002api.utils.ControllerUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CardController {
    private final CardService cardService;


//
//    /**
//     * search with pagination
//     *
//     * @param pageNumber page number
//     * @param pageSize   page item count
//     * @param filterOr   string filter or conditions
//     * @param filterAnd  string filter and conditions
//     * @param orderBy    sting order items by
//     * @return ResponseEntity with PageResponseDTO for {@link CardSet}
//     */
//    @GetMapping("/{gameName}/set/page")
//    public ResponseEntity<?> fetchSetByPages(
//            @PathVariable String gameName,
//            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
//            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
//            @RequestParam(value = "filterOr", required = false) String filterOr,
//            @RequestParam(value = "filterAnd", required = false) String filterAnd,
//            @RequestParam(value = "orderBy", required = false) String orderBy) {
//        // initialize variable to be returned
//        try {
//            PageResponseDTO<CardSet> responseBody = new PageResponseDTO<>();
//            QueryWithPageDTO serviceRequest = new ControllerUtils().generateFilterAndPaginationRepositoryQuery(
//                    pageSize, pageNumber, orderBy, filterAnd, filterOr);
//            Page<CardSet> page = cardService.getSetPages(serviceRequest);
//            responseBody.setPageStats(page, page.getContent());
//            return new ResponseEntity<>(responseBody, HttpStatus.OK);
//        } catch (BadRequestException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    /**
//     * search without pagination
//     *
//     * @param filterOr   string filter or conditions
//     * @param filterAnd  string filter and conditions
//     * @return ResponseEntity with List of {@link CardSet}
//     */
//    @GetMapping("/{gameName}/set")
//    public ResponseEntity<?> fetchSet(
//            @PathVariable String gameName,
//            @RequestParam(value = "filterOr", required = false) String filterOr,
//            @RequestParam(value = "filterAnd", required = false) String filterAnd){
//        // initialize variable to be returned
//        try {
//
//            Query serviceRequest = new ControllerUtils().generateFilterRepositoryQuery(
//                    filterAnd, filterOr);
//            List<CardSet> responseBody = cardService.getSets(serviceRequest);
//            return new ResponseEntity<>(responseBody, HttpStatus.OK);
//        } catch (BadRequestException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//


    @PostMapping("/{gameName}/card")
    public ResponseEntity<?> addCard(
            @PathVariable GameName gameName,
            @RequestBody String request) {
        try {
            return new ResponseEntity<>(cardService.addCard(gameName, request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    /**
     * search with pagination
     *
     * @param pageNumber page number
     * @param pageSize   page item count
     * @param filterOr   string filter or conditions
     * @param filterAnd  string filter and conditions
     * @param orderBy    sting order items by
     * @return ResponseEntity with PageResponseDTO for {@link CardSet}
     */
    @GetMapping("/{gameName}/card/page")
    public ResponseEntity<?> fetchSetByPages(
            @PathVariable String gameName,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd,
            @RequestParam(value = "orderBy", required = false) String orderBy) {
        try {
            // initialize variable to be returned
            PageResponseDTO<CahCard> responseBody = new PageResponseDTO<>();
            // append gameName filter to filters
            filterAnd=filterAnd.concat(String.format(
                    "%sgameName|eq|%s",
                    filterAnd.length()>0 ? "&" : "",
                    gameName));
            // generate request for cardService
            QueryWithPageDTO serviceRequest = new ControllerUtils().generateFilterAndPaginationRepositoryQuery(
                    pageSize, pageNumber, orderBy, filterAnd, filterOr);
            // get paginated response
            Page<CahCard> page = cardService.getCardPages(serviceRequest);
            // append stats to page section
            responseBody.setPageStats(page, page.getContent());
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * search without pagination
     *
     * @param filterOr   string filter or conditions
     * @param filterAnd  string filter and conditions
     * @return ResponseEntity with List of {@link CardSet}
     */
    @GetMapping("/{gameName}/card")
    public ResponseEntity<?> fetchSet(
            @PathVariable String gameName,
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd){
        // initialize variable to be returned
        try {
            // append gameName filter to filters
            filterAnd=filterAnd.concat(String.format(
                    "%sgameName|eq|%s",
                    filterAnd.length()>0 ? "&" : "",
                    gameName));
            Query serviceRequest = new ControllerUtils().generateFilterRepositoryQuery(
                    filterAnd, filterOr);
            List<CahCard> responseBody = cardService.getCards(serviceRequest);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//
//    /**
//     * update Set with id
//     *
//     * @param gameName gameName
//     * @param id set id
//     * @param setData new set data
//     * @return CardSet
//     */
//    @PatchMapping("/{gameName}/set/{id}")
//    public ResponseEntity<?> updateSet(
//            @PathVariable GameName gameName,
//            @PathVariable String id,
//            @RequestBody CardSet setData) {
//        try {
//            return new ResponseEntity<>(cardService.updateSet(id, setData, gameName), HttpStatus.OK);
//        } catch (CardSetCollectionException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (ConstraintViolationException | ImmutableFieldException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
//        }
//    }
//
//    /**
//     *
//     * delete set from DB
//     * @param gameName game name
//     * @param id set id
//     * @return ResponseEntity
//     * @throws CardSetCollectionException Not Found error
//     */
//    @DeleteMapping("{gameName}/set/{id}")
//    public ResponseEntity<?> deleteSetById(
//            @PathVariable GameName gameName,
//            @PathVariable String id) throws CardSetCollectionException {
//        try {
//            cardService.deleteSet(id);
//            return new ResponseEntity<>("Successfully deleted set with id " + id, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }
//
}
