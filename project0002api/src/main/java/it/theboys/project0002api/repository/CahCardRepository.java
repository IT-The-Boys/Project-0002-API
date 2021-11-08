package it.theboys.project0002api.repository;

import it.theboys.project0002api.model.database.CardSet;
import it.theboys.project0002api.model.database.cah.CahCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CahCardRepository extends BaseRepository<CahCard, String> {
    Optional<CahCard> findCahCardByCardSetAndAndCardText(CardSet cardSet, String cardText);

    List<CahCard> findCahCardByCardSet(CardSet cardSet);

    Page<CahCard> findPagedCahCardByCardSet(CardSet cardSet, Pageable pageable);

}
