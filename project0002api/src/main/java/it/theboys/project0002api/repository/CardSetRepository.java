package it.theboys.project0002api.repository;

import it.theboys.project0002api.enums.GameName;
import it.theboys.project0002api.model.database.CardSet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardSetRepository
        extends BaseRepository<CardSet, String> {


    Optional<CardSet> findCardSetByGameNameAndSetName(GameName gameName, String setName);
    List<CardSet> findCardSetByGameName(GameName gameName);
}
