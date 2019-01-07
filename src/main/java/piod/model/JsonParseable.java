package piod.model;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface JsonParseable {

    String toJson() throws JsonProcessingException;
}
