/**
 * Created by kunalwagle on 20/04/2017.
 */
import fetch from "isomorphic-fetch";
import {subscriptionTabSelected} from "./SearchResults/SearchResultsActions";

export const EMAIL_CHANGED = 'EMAIL_CHANGED';
export const LOG_IN_CHANGE = 'LOG_IN_CHANGE';
export const FETCH_STARTED = 'FETCH_STARTED';
export const FETCH_ENDED = 'FETCH_ENDED';
export const SUBSCRIBE_COMPLETE = 'SUBSCRIBE_COMPLETE';


export function emailAddressChanged(text) {
    return {
        type: EMAIL_CHANGED,
        text
    };
}

export function loginChanged(loggedIn) {
    return {
        type: LOG_IN_CHANGE,
        loggedIn
    };
}

export function subscribe(topic) {
    return (dispatch, getState) => {
        const email = getState().loggedIn.email;
        return fetch("http://localhost:8182/api/user/subscribe/" + email + "/" + topic)
            .then(dispatch(subscribeComplete()))
    }
}

export function subscribeComplete() {
    return {
        type: SUBSCRIBE_COMPLETE
    }
}

export function getSubscriptions() {
    return (dispatch, getState) => {
        dispatch(startSubscriptionFetch());
        const email = getState().loggedIn.email;
        return fetch("http://localhost:8182/api/user/subscriptions/" + email)
            .then(response => response.json())
            .then(json => dispatch(fetchedSubscriptions(json)));
    }
}

export function startSubscriptionFetch() {
    return {
        type: FETCH_STARTED
    }
}

export function fetchedSubscriptions(json) {
    return (dispatch) => {
        if (json.length > 0) {
            const articles = json[0].clusterHolder;
            dispatch(subscriptionTabSelected(articles));
        }
        return dispatch(populateSubscriptions(json));
    };
}

export function populateSubscriptions(json) {
    return {
        type: FETCH_ENDED,
        json
    };
}



