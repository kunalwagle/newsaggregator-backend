/**
 * Created by kunalwagle on 20/04/2017.
 */
import {EMAIL_CHANGED, LOG_IN_CHANGE, FETCH_ENDED, FETCH_STARTED} from "../actions/LoginModalActions";

const initialState = {
    loggedIn: false,
    email: "",
    fetchInProgress: false,
    topics: []
};

export default function loggedIn(state, action) {
    if (typeof state === 'undefined') {
        return initialState;
    }

    switch (action.type) {
        case EMAIL_CHANGED:
            return Object.assign({}, state, {
                email: action.text
            });
        case LOG_IN_CHANGE:
            let email = state.email;
            if (!action.loggedIn) {
                email = "";
            }
            return Object.assign({}, state, {
                loggedIn: action.loggedIn,
                email: email
            });
        case FETCH_STARTED:
            return Object.assign({}, state, {
                fetchInProgress: true
            });
        case FETCH_ENDED:
            return Object.assign({}, state, {
                topics: action.json,
                fetchInProgress: false
            });
        default:
            return state
    }

}