package it.theboys.project0002api.model.database;

import it.theboys.project0002api.enums.GameName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "cardThemes")
public class CardTheme {
    GameName gameName;
    String cardFaceUp;
    String cardFaceDown;
    String themeName;
}
