package it.theboys.project0002api.model.database.cah;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.theboys.project0002api.enums.GameName;
import it.theboys.project0002api.enums.cah.CahCardType;
import it.theboys.project0002api.model.database.CardEdition;
import it.theboys.project0002api.model.database.CardSet;
import it.theboys.project0002api.model.database.CardTheme;
import it.theboys.project0002api.utils.json.deserializer.CahCardDeserializer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@CompoundIndexes({
        @CompoundIndex(name = "set_name_idx", def = "{'cardSet' : 1, 'cardName': 1}", unique = true)
})
@JsonDeserialize(using = CahCardDeserializer.class)

@Document(collection = "CAHCards")
public class CahCard {
    @Id
    private String cardId;
    @NotNull
    private GameName gameName = GameName.CAH;
    @NotNull
    @DBRef
    private CardSet cardSet;
    @NotNull
    private String cardText;
    @NotNull
    private CahCardType cardType;
    private CahAction cardActions;
    @DBRef
    private CardTheme cardTheme;
    @DBRef
    private CardEdition cardEdition;
    private long addedAt;
    private long updatedAt;

}
