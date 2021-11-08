package it.theboys.project0002api.utils.database;

import it.theboys.project0002api.dto.database.FilterConditionDto;
import it.theboys.project0002api.enums.FilterOperationEnum;
import it.theboys.project0002api.exception.database.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility for Filtering Page
 * This class is used to extract any filters requested by the client.
 */
public class FilterBuilderUtils {
    private static final int DEFAULT_SIZE_PAGE = 20;

    /**
     * Get request pageable. Page Request Builder. custom pageable
     *
     * @param pageSize  the number of items to collect
     * @param pageNumber  page number
     * @param orderBy search order filter (eg: field|ASC)
     * @return PageRequest
     */
    public PageRequest getPageable(int pageSize, int pageNumber, String orderBy) {
        //check @params for pageSize and pageNumber for  null
        int itemsPerPage = (pageSize <= 0) ? DEFAULT_SIZE_PAGE : pageSize;
        int currentPage = (pageNumber <= 0) ? 1 : pageNumber;
        try
        {
            // check if ordering is requested
            if (orderBy!=null && !orderBy.isEmpty()){
                final String FILTER_CONDITION_DELIMITER = "\\|";
                //get ordering conditions
                List<String> values = splitString(orderBy, FILTER_CONDITION_DELIMITER);
                String column = values.get(0);
                String sortDirection = values.get(1);
                //order in ascending or descending order
                if (sortDirection.equalsIgnoreCase("ASC")) {
                    return PageRequest.of((currentPage - 1), pageSize, Sort.by(Sort.Direction.ASC, column));
                } else if (sortDirection.equalsIgnoreCase("DESC")) {
                    return PageRequest.of((currentPage - 1), pageSize, Sort.by(Sort.Direction.DESC, column));
                } else {
                    throw new IllegalArgumentException(String.format("Value for param 'order' is not valid : %s , must be 'asc' or 'desc'", sortDirection));
                }

            } else {
                return PageRequest.of((currentPage - 1), itemsPerPage);
            }
        } catch (Exception e){
            throw new BadRequestException("Cannot create condition filter " + e.getMessage());
        }

    }

    // Custom String splitter
    private static List<String> splitString(String search, String delimiter) {
        return Stream.of(search.split(delimiter))
                .collect(Collectors.toList());
    }

    /**
     * Prepare filter condition.  extract the different filters used in the controller via @RequestParam
     *
     * @param criteria search Criteria.
     * @return a list of {@link FilterConditionDto}
     */
    public List<FilterConditionDto> createFilter(String criteria) {
        // initialize empty List to be returned
        List<FilterConditionDto> filters = new ArrayList<>();
        try {

            if (criteria != null && !criteria.isEmpty()) {

                final String FILTER_SEARCH_DELIMITER = "&";
                final String FILTER_CONDITION_DELIMITER = "\\|";
                // create params from criteria
                List<String> values = splitString(criteria, FILTER_SEARCH_DELIMITER);
                // check if there is criteria
                if (!values.isEmpty()) {
                    values.forEach(x -> {
                        List<String> filter = splitString(x, FILTER_CONDITION_DELIMITER);
                        // check if there is condition in criteria
                        if (FilterOperationEnum.fromValue(filter.get(1)) != null) {
                            filters.add(
                                    new FilterConditionDto(filter.get(0),
                                            FilterOperationEnum.fromValue(filter.get(1)),
                                            filter.get(2)));
                        }
                    });
                }
            }
            return filters;

        } catch (Exception e) {
            throw new BadRequestException("Cannot create condition filter " + e.getMessage());
        }

    }
}
