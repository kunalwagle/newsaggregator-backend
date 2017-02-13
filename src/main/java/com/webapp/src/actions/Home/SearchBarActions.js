/**
 * Created by kunalwagle on 06/02/2017.
 */
import fetch from "isomorphic-fetch";

export const SEARCH_VALUE_CHANGED = 'SEARCH_VALUE_CHANGED';
export const SEARCH_START = 'SEARCH_START';
export const SEARCH_RESULTS_RECEIVED = 'SEARCH_RESULTS_RECEIVED';

export function searchValueChanged(searchValue) {
    return {
        type: SEARCH_VALUE_CHANGED,
        searchValue
    }
}

export function search() {
    return (dispatch, getState) => {
        dispatch(searchStarted());
        return fetch('http://localhost:8182/api/wikipedia/' + getState().searchBar.searchTerm)
            .then(response => response.json())
            .then(json => dispatch(searchResultsReceived(json)))
    }
}

export function searchStarted() {
    return {
        type: SEARCH_START
    }
}

export function searchResultsReceived(json) {
    return {
        type: SEARCH_RESULTS_RECEIVED,
        json
    }
}