package it.theboys.project0002api.model.database;

import it.theboys.project0002api.enums.GameName;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="CardEditions")
public class CardEdition {
    private GameName gameName;
    private String editionName;
    private String editionVersion;

}
