/**
 * Created by kunalwagle on 04/04/2017.
 */
import fetch from "isomorphic-fetch";

export const FIRST_CHANGED = 'FIRST_CHANGED';
export const SECOND_CHANGED = 'SECOND_CHANGED';
export const THIRD_CHANGED = 'THIRD_CHANGED';
export const SUMMARY_START = 'SUMMARY_START';
export const SUMMARY_RESULTS_RECEIVED = 'SUMMARY_RESULTS_RECEIVED';

export function firstChanged(newValue) {
    return {
        type: FIRST_CHANGED,
        searchValue
    }
}

export function secondChanged(newValue) {
    return {
        type: SECOND_CHANGED,
        searchValue
    }
}

export function thirdChanged(newValue) {
    return {
        type: THIRD_CHANGED,
        searchValue
    }
}

export function summarise() {
    return (dispatch, getState) => {
        dispatch(summariseStarted());
        return fetch('http://localhost:8182/api/wikipedia/' + getState().searchBar.searchTerm)
            .then(response => response.json())
            .then(json => dispatch(summaryResultsReceived(json)))
    }
}

export function summariseStarted() {
    return {
        type: SUMMARY_START
    }
}

export function summaryResultsReceived(json) {
    return {
        type: SUMMARY_RESULTS_RECEIVED,
        json
    }
}
