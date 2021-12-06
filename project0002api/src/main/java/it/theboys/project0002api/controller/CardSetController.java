package it.theboys.project0002api.controller;


import java.util.List;

import javax.validation.ConstraintViolationException;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.theboys.project0002api.dto.database.QueryWithPageDTO;
import it.theboys.project0002api.enums.GameName;
import it.theboys.project0002api.exception.database.BadRequestException;
import it.theboys.project0002api.exception.database.CardSetCollectionException;
import it.theboys.project0002api.exception.database.ImmutableFieldException;
import it.theboys.project0002api.model.database.CardSet;
import it.theboys.project0002api.model.database.cah.CahCard;
import it.theboys.project0002api.utils.ControllerUtils;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CardSetController<CardService> {

    private final CardService cardService;

    /**
     * search with pagination
     *
     * @param pageNumber page number
     * @param pageSize   page item count
     * @param filterOr   string filter or conditions
     * @param filterAnd  string filter and conditions
     * @param orderBy    sting order items by
     * @return ResponseEntity with PageResponseDto for {@link CardSet}
     */
    @GetMapping("/{gameName}/set/page")
    public ResponseEntity<?> fetchSetsByPages(
            @PathVariable String gameName,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd,
            @RequestParam(value = "orderBy", required = false) String orderBy) {
        // initialize variable to be returned
        try {
            PageResponseDto<CardSet> responseBody = new PageResponseDto<>();
            // append gameName filter to filters
            filterAnd = filterAnd.concat(String.format(
                    "%sgameName|eq|%s",
                    filterAnd.length() > 0 ? "&" : "",
                    gameName));
            QueryWithPageDTO serviceRequest = new ControllerUtils().generateFilterAndPaginationRepositoryQuery(
                    pageSize, pageNumber, orderBy, filterAnd, filterOr, "setName");
            Page<CardSet> page = cardService.getSetPages(serviceRequest);
            responseBody.setPageStats(page, page.getContent());
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * search without pagination
     *
     * @param filterOr  string filter or conditions
     * @param filterAnd string filter and conditions
     * @return ResponseEntity with List of {@link CardSet}
     */
    @GetMapping("/{gameName}/set")
    @JsonView(CardSetView.IdNameDescExp.class)
    public ResponseEntity<?> fetchSets(
            @PathVariable GameName gameName,
            @RequestParam(value = "setList", required = false) String[] setList,
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd) {
        //check if setlist param exist
        if(setList!=null){
            return new ResponseEntity<>(cardService.getSetByIdList(gameName,setList), HttpStatus.OK);
        }
        // initialize variable to be returned
        try {

            Query serviceRequest = new ControllerUtils().generateFilterRepositoryQuery(
                    filterAnd, filterOr, "setName");
            // append gameName filter to filters
            filterAnd = filterAnd.concat(String.format(
                    "%sgameName|eq|%s",
                    filterAnd.length() > 0 ? "&" : "",
                    gameName));
            List<CardSet> responseBody = cardService.getSets(serviceRequest);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get Set and all sen cards
     *
     * @param gameName game name
     * @param id       set id
     * @return ResponseEntity with results
     * @throws CardSetCollectionException CardSet Validation Errors
     */
    @GetMapping("/{gameName}/set/{id}/page")
    @JsonView(CardSetView.IdNameDescExp.class)
    public ResponseEntity<?> fetchSetPage(
            @PathVariable String gameName,
            @PathVariable String id,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "orderBy", required = false) String orderBy
    ) throws CardSetCollectionException {
        PagedSetWithCardsResponseDto<CahCard, CardSet> responseBody = new PagedSetWithCardsResponseDto<>();
        try {
            Pageable pageable = new ControllerUtils().generatePagination(
                    pageSize, pageNumber, orderBy);
            responseBody = cardService.getSetPageById(id, pageable);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{gameName}/set/{id}")
    @JsonView(CardSetView.IdNameDescExp.class)
    public ResponseEntity<?> fetchSet(
            @PathVariable String gameName,
            @PathVariable String id
    ) throws CardSetCollectionException {
        try {
            return new ResponseEntity<>(cardService.getSetById(id), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    /**
     * @param gameName game name
     * @param json     set data in json
     * @return List of saved {@link CardSet}
     * @throws CardSetCollectionException CardSet validation error
     */
    @PostMapping("/{gameName}/set")
    public ResponseEntity<?> addSet(
            @PathVariable GameName gameName,
            @RequestBody String json) throws CardSetCollectionException {
        try {
            return new ResponseEntity<>(cardService.addSet(gameName, json), HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (CardSetCollectionException | JsonProcessingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (ConversionFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }


    /**
     * update Set with id
     *
     * @param gameName gameName
     * @param id       set id
     * @param setData  new set data
     * @return CardSet
     */
    @PatchMapping("/{gameName}/set/{id}")
    public ResponseEntity<?> updateSet(
            @PathVariable GameName gameName,
            @PathVariable String id,
            @RequestBody CardSet setData) {
        try {
            return new ResponseEntity<>(cardService.updateSet(id, setData, gameName), HttpStatus.OK);
        } catch (CardSetCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ConstraintViolationException | ImmutableFieldException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (ConversionFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * delete set from DB
     *
     * @param gameName game name
     * @param id       set id
     * @return ResponseEntity
     * @throws CardSetCollectionException Not Found error
     */
    @DeleteMapping("{gameName}/set/{id}")
    public ResponseEntity<?> deleteSetById(
            @PathVariable GameName gameName,
            @PathVariable String id) throws CardSetCollectionException {
        try {
            cardService.deleteSet(id);
            return new ResponseEntity<>("Successfully deleted set with id " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}