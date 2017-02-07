/**
 * Created by kunalwagle on 06/02/2017.
 */
import {SEARCH_VALUE_CHANGED, SEARCH_START, SEARCH_RESULTS_RECEIVED} from "../../actions/Home/SearchBarActions";

const initialState = {
    searchTerm: "",
    fetchInProgress: false,
    searchResults: []
};

export default function searchBar(state, action) {
    if (typeof state === 'undefined') {
        return initialState;
    }

    switch (action.type) {
        case SEARCH_VALUE_CHANGED:
            return Object.assign({}, state, {
                searchTerm: action.searchValue
            });
        case SEARCH_START:
            return Object.assign({}, state, {
                fetchInProgress: true,
            });
        case SEARCH_RESULTS_RECEIVED:
            return Object.assign({}, state, {
                fetchInProgress: false,
                searchResults: action.json
            })
    }

    return state;
}