package it.theboys.project0002api.model.database;

import it.theboys.project0002api.enums.GameName;
import it.theboys.project0002api.enums.cah.CahSetExpansion;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Document(collection = "cardSets")
@CompoundIndexes({
        @CompoundIndex(name = "game_set_idx", def = "{'gameName' : 1, 'setName': 1}", unique = true)
})
public class CardSet {
    @Id
    private String id;
    @NotNull(message="Please provide game name")
    private GameName gameName;
    @NotNull(message="Please provide set name")
    private String setName;
    private String setDescription;
    private int setAgeRestrictions=17;
    @NotNull
    private CahSetExpansion setExpansion;
    private boolean isPrivate;
    private long addedAt;
    private long updatedAt;
}
