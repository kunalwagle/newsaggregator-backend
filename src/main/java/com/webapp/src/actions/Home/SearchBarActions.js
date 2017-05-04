/**
 * Created by kunalwagle on 06/02/2017.
 */
import fetch from "isomorphic-fetch";
import {push} from "react-router-redux";
import {getIPAddress} from "../../UtilityMethods";

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
        let searchTerm = getState().searchBar.searchTerm.replace(' ', "%20");
        dispatch(push("/searchResults/" + searchTerm));
        return fetch(getIPAddress() + 'wikipedia/' + searchTerm)
            .then(response => response.json())
            .then(json => dispatch(searchResultsReceived(json)))
    }
}

export function searchOnReload(searchTerm) {
    return (dispatch) => {
        dispatch(searchStarted());
        return fetch(getIPAddress() + 'wikipedia/' + searchTerm)
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
