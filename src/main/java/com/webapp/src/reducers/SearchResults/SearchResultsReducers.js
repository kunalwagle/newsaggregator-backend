/**
 * Created by kunalwagle on 19/04/2017.
 */
import {
    VIEW_START,
    ARTICLES_RECEIVED,
    SUBSCRIPTION_TAB_SELECTED
} from "../../actions/SearchResults/SearchResultsActions";

const initialState = {
    label: "",
    fetchInProgress: false,
    fetchInProgressCalled: false,
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
                fetchInProgressCalled: true,
                articles: []
            });
        case ARTICLES_RECEIVED:
            return Object.assign({}, state, {
                fetchInProgress: false,
                articles: action.json
            });
        case SUBSCRIPTION_TAB_SELECTED:
            return Object.assign({}, state, {
                articles: action.articles
            })
    }

    return state;
}