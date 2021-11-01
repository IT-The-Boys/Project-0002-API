package it.theboys.project0002api.dto.database;

import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

@Data
public class QueryWithPageDTO {
    private Query query;
    private Pageable pageable;
}
