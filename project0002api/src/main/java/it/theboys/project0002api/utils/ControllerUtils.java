package it.theboys.project0002api.utils;

import it.theboys.project0002api.dto.database.FilterConditionDto;
import it.theboys.project0002api.dto.database.QueryWithPageDTO;
import it.theboys.project0002api.utils.database.FilterBuilderUtils;
import it.theboys.project0002api.utils.database.MongoQueryBuilderUtils;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class ControllerUtils {

    /**
     * generate Mongodb Query with filters and pagination
     *
     * @param pageSize   page item count
     * @param pageNumber page number
     * @param orderBy    sting order items by
     * @param filterOr   string filter or conditions
     * @param filterAnd  string filter and conditions
     *
     * @return QueryWithPageDto
     */
    public QueryWithPageDTO generateFilterAndPaginationRepositoryQuery(
            int pageSize,
            int pageNumber,
            String orderBy,
            String filterAnd,
            String filterOr)
    {
        QueryWithPageDTO result = new QueryWithPageDTO();
        FilterBuilderUtils filterBuilder = new FilterBuilderUtils();
        // create pagination for query
        result.setPageable(filterBuilder.getPageable(pageSize, pageNumber, orderBy));
        MongoQueryBuilderUtils queryBuilder = new MongoQueryBuilderUtils();
        // create list of Conditions
        List<FilterConditionDto> andConditions=filterBuilder.createFilter(filterAnd);
        List<FilterConditionDto> orConditions=filterBuilder.createFilter(filterOr);
        // create mongodb query for db by adding filter condition
        result.setQuery(queryBuilder.addCondition(andConditions, orConditions));
        return result;
    }

    /**
     * generate Mongodb Query with filters
     *
     * @param filterAnd string filter and conditions
     * @param filterOr string filter or conditions
     * @return
     */
    public Query generateFilterRepositoryQuery(String filterAnd, String filterOr) {
        FilterBuilderUtils filterBuilder = new FilterBuilderUtils();
        MongoQueryBuilderUtils queryBuilder = new MongoQueryBuilderUtils();
        // create list of Conditions
        List<FilterConditionDto> andConditions=filterBuilder.createFilter(filterAnd);
        List<FilterConditionDto> orConditions=filterBuilder.createFilter(filterOr);
        // create mongodb query for db by adding filter condition
        return queryBuilder.addCondition(andConditions, orConditions);
    }
}
