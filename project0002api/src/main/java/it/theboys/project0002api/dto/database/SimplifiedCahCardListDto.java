package it.theboys.project0002api.dto.database;

import java.util.List;

import it.theboys.project0002api.model.database.cah.CahCard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@JsonSerialize(using=SimplifiedCahCardSerializer.class)
public class SimplifiedCahCardListDto {
    List<CahCard> cardList;
}