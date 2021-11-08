package it.theboys.project0002api.utils.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import it.theboys.project0002api.dto.http.response.SetWithCardsResponseDto;
import it.theboys.project0002api.model.database.CardSet;
import it.theboys.project0002api.model.database.cah.CahCard;

import java.io.IOException;

public class SetWithCardsResponseSerializer extends StdSerializer<SetWithCardsResponseDto> {


    protected SetWithCardsResponseSerializer() {
        super(SetWithCardsResponseDto.class);
    }

    @Override
    public void serialize(SetWithCardsResponseDto value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("cardSet", value.getSetInfo());
        gen.writeArrayFieldStart("cardList");
        for (CahCard card: value.getCardList()) {
            gen.writeStartObject();
            gen.writeStringField("cardId", card.getCardId());
            gen.writeStringField("gameName", card.getGameName().getDesc());
            gen.writeStringField("cardType", card.getCardType().toString());
            gen.writeStringField("cardText", card.getCardText());
            gen.writeObjectField("cardActions", card.getCardActions());
            gen.writeEndObject();
        }
        gen.writeEndObject();
    }
}