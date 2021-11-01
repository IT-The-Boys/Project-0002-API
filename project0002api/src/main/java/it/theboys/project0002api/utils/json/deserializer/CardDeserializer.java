package it.theboys.project0002api.utils.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.theboys.project0002api.model.database.cah.CahCard;

import java.io.IOException;
import java.lang.reflect.Method;

public class CardDeserializer extends JsonDeserializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CahCard deserialize(JsonParser p, DeserializationContext cxt) throws IOException, JsonProcessingException {
        CahCard card = new CahCard();
        Method[] methods = card.getClass().getMethods();
        JsonNode nodeList = p.getCodec().readTree(p);
        for (Method method : methods) {
            String field = generateField(method);
            JsonNode node = nodeList.get(field);
            if (node != null) {
                try {

                    method.invoke(card, objectMapper.treeToValue(node, method.getParameterTypes()[0]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return card;
    }

    private String generateField(Method method) {
        String methodName = method.getName();
        if (methodName.startsWith("set")) {
            String field = methodName.replace("set", "");
            char[] arr = field.toCharArray();
            arr[0] = Character.toLowerCase(arr[0]);
            return String.valueOf(arr);
        }
        return null;
    }


}
