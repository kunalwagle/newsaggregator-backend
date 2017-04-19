/**
 * Created by kunalwagle on 19/04/2017.
 */
import {VIEW_START, ARTICLES_RECEIVED} from "../../actions/SearchResults/SearchResultsActions";

const initialState = {
    label: "",
    fetchInProgress: false,
    articles: []
};

export default function searchResults(state, action) {
    if (typeof state === 'undefined') {
        return initialState;
    }

    switch (action.type) {
        case VIEW_START:
            return Object.assign({}, state, {
                label: action.label,
                fetchInProgress: true,
                articles: []
            });
        case ARTICLES_RECEIVED:
            return Object.assign({}, state, {
                fetchInProgress: false,
                articles: action.json
            })
    }

    return state;
}