/**
 * Created by kunalwagle on 20/04/2017.
 */
import {EMAIL_CHANGED, LOG_IN, LOG_OUT, FETCH_ENDED, FETCH_STARTED} from "../actions/LoginModalActions";
import {REHYDRATE} from "redux-persist/constants";

const initialState = {
    loggedIn: false,
    user: {},
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
        case LOG_IN:
            return Object.assign({}, state, {
                loggedIn: action.loggedIn,
                user: action.user
            });
        case LOG_OUT:
            return Object.assign({}, state, {
                loggedIn: false,
                user: {}
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
        case REHYDRATE:
            const incoming = action.payload.loggedIn;
            if (incoming) return {...state, ...incoming};
            return state;
        default:
            return state
    }

}