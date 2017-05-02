/**
 * Created by kunalwagle on 20/04/2017.
 */
import fetch from "isomorphic-fetch";
import {subscriptionTabSelected} from "./SearchResults/SearchResultsActions";
import {push} from "react-router-redux";

export const EMAIL_CHANGED = 'EMAIL_CHANGED';
export const LOG_IN = 'LOG_IN';
export const LOG_OUT = 'LOG_OUT';
export const FETCH_STARTED = 'FETCH_STARTED';
export const FETCH_ENDED = 'FETCH_ENDED';
export const SUBSCRIBE_COMPLETE = 'SUBSCRIBE_COMPLETE';


export function emailAddressChanged(text) {
    return {
        type: EMAIL_CHANGED,
        text
    };
}

export function login() {
    return (dispatch, getState) => {
        dispatch(startSubscriptionFetch());
        return fetch("http://localhost:8182/api/user/subscriptions/" + getState().loggedIn.email)
            .then(response => response.json())
            .then(json => dispatch(logged(json)))
    }
}

export function logged(user) {
    let loggedIn = true;
    if (user.length > 0) {
        loggedIn = false;
    }
    return {
        type: LOG_IN,
        user,
        loggedIn
    }
}

export function logout() {
    return {
        type: LOG_OUT
    }
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
        let articles = [];
        if (json.id !== undefined) {
            dispatch(push("/subscription/" + json.id));
            if (json.topics.length > 0) {
                articles = json.topics[0].clusterHolder;
            }
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



