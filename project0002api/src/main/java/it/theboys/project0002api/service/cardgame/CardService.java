package it.theboys.project0002api.service.cardgame;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.theboys.project0002api.dto.database.QueryWithPageDTO;
import it.theboys.project0002api.dto.http.request.AddCardRequestDto;
import it.theboys.project0002api.enums.GameName;
import it.theboys.project0002api.exception.database.CardSetCollectionException;
import it.theboys.project0002api.exception.database.ImmutableFieldException;
import it.theboys.project0002api.model.database.CardSet;
import it.theboys.project0002api.model.database.cah.CahCard;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

public interface CardService {

    /**
     * Get all custom paginate data for entity CardSet
     *
     * @param request {@link QueryWithPageDTO}
     * @return Page of entity CardSet {@link CardSet}
     */
    Page<CardSet> getSetPages(QueryWithPageDTO request);

    /**
     * Get all custom data for entity CardSet
     *
     * @param serviceRequest custom mongoDB Query
     * @return list of entity Cardset {@link CardSet}
     */
    List<CardSet> getSets(Query serviceRequest);


    /**
     * Get all custom paginate data for entity CardSet
     *
     * @param gameName game Name
     * @param json     request body json{}
     * @return Map<String, List < CardSet>>
     */
    Map<String, List<CardSet>> addSet(GameName gameName, String json) throws CardSetCollectionException, JsonProcessingException;


    /**
     * Return Set Info and all cards in this set
     *
     * @param id set ID
     * @return CardSet
     */
    CardSet getSetById(String id) throws CardSetCollectionException;

    /**
     * Update Set with ID by new data
     *
     * @param id       set ID
     * @param gameName game name
     * @param setData  new data
     * @return CardSet
     */
    CardSet updateSet(String id, CardSet setData, GameName gameName) throws CardSetCollectionException, ImmutableFieldException;

    /**
     * Delete Set by ID
     *
     * @param id set ID
     */
    void deleteSet(String id) throws CardSetCollectionException;

    AddCardRequestDto addCard(GameName gameName, String json) throws JsonProcessingException, CardSetCollectionException;

    /**
     * Get all custom paginate data for entity CardSet
     *
     * @param request {@link QueryWithPageDTO}
     * @return Page of entity CardSet {@link CardSet}
     */
    Page<CahCard> getCardPages(QueryWithPageDTO request);

    /**
     * Get all custom data for entity CardSet
     *
     * @param serviceRequest custom mongoDB Query
     * @return list of entity Cardset {@link CardSet}
     */
    List<CahCard> getCards(Query serviceRequest);


}
