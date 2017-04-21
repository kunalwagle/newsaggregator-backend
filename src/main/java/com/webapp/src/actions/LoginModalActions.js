/**
 * Created by kunalwagle on 20/04/2017.
 */
import fetch from "isomorphic-fetch";

export const EMAIL_CHANGED = 'EMAIL_CHANGED';
export const LOG_IN_CHANGE = 'LOG_IN_CHANGE';
export const SUBSCRIBE = 'SUBSCRIBE';
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
