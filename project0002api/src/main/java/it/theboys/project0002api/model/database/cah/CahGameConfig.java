package it.theboys.project0002api.model.database.cah;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import it.theboys.project0002api.model.database.CardSet;
import it.theboys.project0002api.utils.json.serializer.CahGameConfigSerializer;
import lombok.Data;

@Data
@JsonSerialize(using = CahGameConfigSerializer.class)
public class CahGameConfig {
    int[] playerLimit = {3, 10};
    String serverName;
    String serverPassword;
    int timeLimit = 60;
    int scoreLimit = 8;
    List<CardSet> setList;
}
