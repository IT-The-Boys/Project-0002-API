package it.theboys.project0002api.utils.database;

import it.theboys.project0002api.dto.database.FilterConditionDto;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class is used to build all the queries passed as parameters.
 * filterAndConditions (filter list for the AND operator)
 * filterOrConditions (filter list for the OR operator)
 */

public class MongoQueryBuilderUtils {
    private static final Map<String, Function<FilterConditionDto, Criteria>> FILTER_CRITERIA = new HashMap<>();

    // Initialize map of filter criteria
    static {
        FILTER_CRITERIA.put("EQUAL", condition -> Criteria.where(condition.getField()).is(condition.getValue()));
        FILTER_CRITERIA.put("NOT_EQUAL", condition -> Criteria.where(condition.getField()).ne(condition.getValue()));
        FILTER_CRITERIA.put("GREATER_THAN", condition -> Criteria.where(condition.getField()).gt(condition.getValue()));
        FILTER_CRITERIA.put("GREATER_THAN_OR_EQUAL_TO", condition -> Criteria.where(condition.getField()).gte(condition.getValue()));
        FILTER_CRITERIA.put("LESS_THAN", condition -> Criteria.where(condition.getField()).lt(condition.getValue()));
        FILTER_CRITERIA.put("LESS_THAN_OR_EQUAL_TO", condition -> Criteria.where(condition.getField()).lte(condition.getValue()));
        FILTER_CRITERIA.put("CONTAINS", condition -> Criteria.where(condition.getField()).regex((String) condition.getValue()));
        FILTER_CRITERIA.put("JOIN", condition -> Criteria.where(condition.getField()).is(condition.getValue()));
    }

    private final List<FilterConditionDto> filterAndConditions;
    private final List<FilterConditionDto> filterOrConditions;

    public MongoQueryBuilderUtils() {
        this.filterAndConditions = new ArrayList<>();
        this.filterOrConditions = new ArrayList<>();
    }

    /**
     * Create mongoDB query from conditions
     *
     * @param andConditions and conditions
     * @param orConditions  or conditions
     * @return Query
     */
    public Query addCondition(List<FilterConditionDto> andConditions, List<FilterConditionDto> orConditions) {
        // check if conditions isn null
        if (andConditions != null && !andConditions.isEmpty()) {
            filterAndConditions.addAll(andConditions);
        }
        if (orConditions != null && !orConditions.isEmpty()) {
            filterOrConditions.addAll(orConditions);
        }
        // create list of operators for Query
        List<Criteria> andCriteriaOperator = new ArrayList<>();
        List<Criteria> orCriteriaOperator = new ArrayList<>();
        Criteria criteria = new Criteria();
        // build criteria from conditions
        filterAndConditions.stream().map(condition -> andCriteriaOperator.add(buildCriteria(condition))).collect(Collectors.toList());
        filterOrConditions.stream().map(condition -> orCriteriaOperator.add(buildCriteria(condition))).collect(Collectors.toList());

        //check if operators non NULL
        if (!andCriteriaOperator.isEmpty() && !orCriteriaOperator.isEmpty()) {
            return new Query(criteria.andOperator(andCriteriaOperator.toArray(new Criteria[0])).orOperator(orCriteriaOperator.toArray(new Criteria[0])));
        } else if (!andCriteriaOperator.isEmpty()) {
            return new Query(criteria.andOperator(andCriteriaOperator.toArray(new Criteria[0])));
        } else if (!orCriteriaOperator.isEmpty()) {
            return new Query(criteria.orOperator(orCriteriaOperator.toArray(new Criteria[0])));
        } else {
            // if all is empty return default Query
            return new Query();
        }
    }

    /**
     * Build the predicate according to the request
     *
     * @param condition The condition of the filter requested by the query
     * @return {{@link Criteria}}
     */
    private Criteria buildCriteria(FilterConditionDto condition) {
        Function<FilterConditionDto, Criteria>
                function = FILTER_CRITERIA.get(condition.getOperator().name());

        if (function == null) {
            throw new IllegalArgumentException("Invalid function param type: ");
        }

        return function.apply(condition);
    }

}
