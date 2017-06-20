/**
 * Created by kunalwagle on 20/04/2017.
 */
import fetch from "isomorphic-fetch";
import {subscriptionTabSelected, nothing} from "./SearchResults/SearchResultsActions";
import {push} from "react-router-redux";
import {getIPAddress} from "../UtilityMethods";
import {toastr} from "react-redux-toastr";

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

export function login(email, action) {
    return (dispatch, getState) => {
        if (!email) {
            email = getState().loggedIn.email;
        } else {
            dispatch(logged(email));
        }
        if (action) action(email);
        return {
            type: FETCH_STARTED
        };
    }
}

export function logged(email) {
    toastr.success('Success', 'Logged in succesfully');
    return {
        type: LOG_IN,
        email,
        loggedIn: true
    }
}

export function logout() {
    toastr.success('Success', 'Logged out succesfully');
    return {
        type: LOG_OUT
    }
}

export function subscribe(topic, email) {
    return (dispatch, getState) => {
        if (email == undefined) {
            email = getState().loggedIn.email;
        }
        return fetch(getIPAddress() + "user/subscribe/" + email + "/" + topic)
            .then(response => response.json())
            .then(json => dispatch(subscribeComplete(json, "subscribe")))
    }
}

export function unsubscribe(topic, email) {
    return (dispatch, getState) => {
        if (email == undefined) {
            email = getState().loggedIn.email;
        }
        return fetch(getIPAddress() + "user/unsubscribe/" + email + "/" + topic)
            .then(response => response.json())
            .then(json => dispatch(subscribeComplete(json, "unsubscribe")))
    }
}

export function subscribeComplete(json, subscribeType) {
    if (json) {
        toastr.success("Success", "Successfully " + subscribeType + "d this topic");
    } else {
        toastr.error("Error", "Couldn't succesfully " + subscribeType + " this topic");
    }
    return {
        type: SUBSCRIBE_COMPLETE,
        json
    }
}

export function getSubscriptions(alreadyLoaded) {
    return (dispatch, getState) => {
        dispatch(startSubscriptionFetch());
        if (!alreadyLoaded) {
            dispatch(push("/subscription"));
        }
        const email = getState().loggedIn.email;
        return fetch(getIPAddress() + "user/subscriptions/" + email)
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
    return (dispatch, getState) => {
        dispatch(populateSubscriptions(json));
        if (json.id !== undefined) {
            dispatch(subscriptionTabSelected());
        }
        return dispatch(nothing())

    };
}

export function populateSubscriptions(json) {
    return {
        type: FETCH_ENDED,
        json
    };
}



