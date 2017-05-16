/**
 * Created by kunalwagle on 19/04/2017.
 */
import {
    VIEW_START,
    ARTICLES_RECEIVED,
    SUBSCRIPTION_TAB_SELECTED
} from "../../actions/SearchResults/SearchResultsActions";
import {SUBSCRIBE_COMPLETE} from "../../actions/LoginModalActions";
import {allOutlets} from "../../UtilityMethods";


const initialState = {
    label: "",
    fetchInProgress: false,
    fetchInProgressCalled: false,
    articles: [],
    activeIndex: 0,
    isSubscribed: false,
    defaultCheckbox: allOutlets
};

export default function searchResults(state, action) {
    if (typeof state === 'undefined') {
        return initialState;
    }

    switch (action.type) {
        case VIEW_START:
            return Object.assign({}, state, {
                fetchInProgress: true,
                fetchInProgressCalled: true,
                articles: []
            });
        case ARTICLES_RECEIVED:
            return Object.assign({}, state, {
                fetchInProgress: false,
                label: action.json.labelHolder.label,
                articles: action.json.labelHolder.clusters,
                isSubscribed: action.json.labelHolder.subscribed,
                defaultCheckbox: action.json.sources
            });
        case SUBSCRIPTION_TAB_SELECTED:
            return Object.assign({}, state, {
                articles: action.articles,
                activeIndex: action.index
            });
        case SUBSCRIBE_COMPLETE:
            return Object.assign({}, state, {
                isSubscribed: action.json.subscribed
            });
    }

    return state;
}