package it.theboys.project0002api.service.cardgame;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.theboys.project0002api.dto.database.QueryWithPageDTO;
import it.theboys.project0002api.dto.http.request.AddCardRequestDto;
import it.theboys.project0002api.dto.http.request.AddSetRequestDto;
import it.theboys.project0002api.dto.http.response.PagedSetWithCardsResponseDto;
import it.theboys.project0002api.dto.http.response.SetWithCardsResponseDto;
import it.theboys.project0002api.enums.GameName;
import it.theboys.project0002api.exception.database.CardSetCollectionException;
import it.theboys.project0002api.exception.database.ImmutableFieldException;
import it.theboys.project0002api.model.database.CardSet;
import it.theboys.project0002api.model.database.cah.CahCard;
import it.theboys.project0002api.repository.CahCardRepository;
import it.theboys.project0002api.repository.CardSetRepository;
import it.theboys.project0002api.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintDeclarationException;
import java.time.Instant;
import java.util.*;


@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardSetRepository setRepo;
    private final CahCardRepository cahRepo;
    private final String[] immutableSetFields=new String[]{"id", "gameName", "addedAt", "updatedAt"};



    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<CardSet>> addSet(GameName gameName, String json) throws ConstraintDeclarationException, CardSetCollectionException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        AddSetRequestDto setList = mapper.readValue(json, AddSetRequestDto.class);
        Map<String, List<CardSet>> responseBody = new HashMap<>();
        List<CardSet> added = new ArrayList<>();
        List<CardSet> existed = new ArrayList<>();
        setList.getSetList().forEach(
                (set) -> {
                    Optional<CardSet> setInDB = setRepo.findCardSetByGameNameAndSetName(gameName, set.getSetName());
                    if (setInDB.isPresent()) {
                        existed.add(set);
                    } else {
                        set.setGameName(gameName);
                        set.setAddedAt(Instant.now().toEpochMilli());
                        added.add(setRepo.save(set));
                    }
                }
        );
        responseBody.put("Success", added);
        responseBody.put("Failure", existed);
        return responseBody;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CardSet> getSetPages(QueryWithPageDTO request) {
        return setRepo.findAll(request.getQuery(), request.getPageable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CardSet> getSets(Query serviceRequest) {
        return setRepo.findAll(serviceRequest);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public PagedSetWithCardsResponseDto<CahCard, CardSet> getSetPageById(String id, Pageable pageable) throws CardSetCollectionException {
        Optional<CardSet> set = setRepo.findById(id);
        if (set.isPresent()) {
            PagedSetWithCardsResponseDto<CahCard, CardSet> responseBody = new PagedSetWithCardsResponseDto<>();
            CardSet setData = set.get();
            responseBody.setInfo(setData);
            Page<CahCard> page=cahRepo.findPagedCahCardByCardSet(setData, pageable);
            responseBody.setPageStats(page, page.getContent());
            return responseBody;
        } else {
            throw new CardSetCollectionException(CardSetCollectionException.NotFoundException(id));
        }
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public SetWithCardsResponseDto getSetById(String id) throws CardSetCollectionException {
        Optional<CardSet> set = setRepo.findById(id);
        if (set.isPresent()) {
            CardSet setData = set.get();
            SetWithCardsResponseDto responseBody=new SetWithCardsResponseDto();
            responseBody.setSetInfo(setData);
            responseBody.setCardList(cahRepo.findCahCardByCardSet(setData));
            return responseBody;
        } else {
            throw new CardSetCollectionException(CardSetCollectionException.NotFoundException(id));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    // TODO add user yo setAuthor
    public CardSet updateSet(String id, CardSet newSetData, GameName gameName) throws CardSetCollectionException, ImmutableFieldException {
        Optional<CardSet> setById = setRepo.findById(id);
        // check if set with @param id is exists
        if (setById.isEmpty()) {
            //if not try add set to db
            newSetData.setGameName(gameName);
            newSetData.setAddedAt(Instant.now().toEpochMilli());
            return setRepo.save(newSetData);
        }
        //check if set with new name exist in this @param gameName
//        Optional<CardSet> setByName = setRepo.findCardSetByGameNameAndSetName(gameName, newSetData.getSetName());
//        if (setByName.isPresent() && !setByName.get().getId().equals(id)) {
//            throw new CardSetCollectionException(CardSetCollectionException.AlreadyExistException(gameName, newSetData.getSetName()));
//        }
        CardSet updatingSet = setById.get();
        CardSet setToSave=new ObjectUtils<CardSet>().updateObjectFromObject(updatingSet,newSetData, immutableSetFields);
        setToSave.setUpdatedAt(Instant.now().toEpochMilli());
        return setRepo.save(setToSave);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSet(String id) throws CardSetCollectionException {
        Optional<CardSet> setById = setRepo.findById(id);
        // check if set exist
        if (setById.isPresent()) {
            // TODO find all cards by set
            List<CahCard> cardsInSet=cahRepo.findCahCardByCardSet(setById.get());
            for (CahCard card: cardsInSet){
                cahRepo.deleteById(card.getCardId());
            }
            // delete set from db
            setRepo.deleteById(id);
        } else {
            throw new CardSetCollectionException(CardSetCollectionException.NotFoundException(id));
        }
    }

    @Override
    public AddCardRequestDto addCard(GameName gameName, String json) throws JsonProcessingException, CardSetCollectionException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        AddCardRequestDto cardList= mapper.readValue(json,AddCardRequestDto.class);
        String setId=cardList.getCardSetId();
        //get set by ID
        Optional<CardSet> cardSet= setRepo.findById(setId);
        if (cardSet.isEmpty()) {
          throw new CardSetCollectionException(CardSetCollectionException.NotFoundException(setId));
        }
        cardList.getCahCards().forEach(card -> {
            Optional<CahCard> cardInDB = cahRepo.findCahCardByCardSetAndAndCardText(cardSet.get(),card.getCardText());
            if (cardInDB.isPresent()) {

            } else {
                // add card set to card instance
                card.setCardSet(cardSet.get());
                card.setAddedAt(Instant.now().toEpochMilli());
                cahRepo.save(card);
            }
        });
        return cardList;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Page<CahCard> getCardPages(QueryWithPageDTO request) {
        return cahRepo.findAll(request.getQuery(), request.getPageable());
    }
    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public List<CahCard> getCards(Query serviceRequest) {
        return cahRepo.findAll(serviceRequest);
    }

    @Override
    public List<CardSet> getAllSets(GameName gameName) {
        return setRepo.findCardSetByGameName(gameName);
    }


}
