package it.theboys.project0002api.utils.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import it.theboys.project0002api.model.database.cah.CahGameConfig;
import it.theboys.project0002api.model.database.CardSet;

import java.io.IOException;

public class CahGameConfigSerializer extends StdSerializer<CahGameConfig> {

    protected CahGameConfigSerializer() {
        super(CahGameConfig.class);
    }

    @Override
    public void serialize(CahGameConfig value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String baseURL="api/v1/CAH/set/";
        gen.writeStartObject();
        gen.writeArrayFieldStart("playerLimit");
        gen.writeNumber(value.getPlayerLimit()[0]);
        gen.writeNumber(value.getPlayerLimit()[1]);
        gen.writeEndArray();
        gen.writeStringField("serverName",value.getServerName());
        gen.writeStringField("serverPassword",value.getServerPassword());
        gen.writeNumberField("timeLimit",value.getTimeLimit());
        gen.writeNumberField("scoreLimit",value.getScoreLimit());
        gen.writeArrayFieldStart("setList");
        for (CardSet set: value.getSetList()) {
            gen.writeStartObject();
            gen.writeStringField("setId", set.getId());
            gen.writeStringField("setName", set.getSetName());
            gen.writeStringField("setInfo", baseURL+set.getId());
            gen.writeEndObject();
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
