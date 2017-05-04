/**
 * Created by kunalwagle on 19/04/2017.
 */
import fetch from "isomorphic-fetch";
import {push} from "react-router-redux";
import {getIPAddress} from "../../UtilityMethods";

export const VIEW_START = 'VIEW_START';
export const ARTICLES_RECEIVED = 'ARTICLES_RECEIVED';
export const SUBSCRIPTION_TAB_SELECTED = 'SUBSCRIPTION_TAB_SELECTED';


export function viewClicked(title) {
    return (dispatch) => {
        dispatch(viewStarted());
        dispatch(push("/topic/" + title));
        return fetch(getIPAddress() + "topic/" + title)
            .then(response => response.json())
            .then(json => dispatch(articlesReceived(json)))
    }
}

export function articleClicked(title) {
    return (dispatch) => {
        dispatch(viewStarted());
        return fetch(getIPAddress() + "topic/" + title)
            .then(response => response.json())
            .then(json => dispatch(articlesReceived(json)))
    }
}

export function viewStarted() {
    return {
        type: VIEW_START
    }
}

export function articlesReceived(json) {
    return {
        type: ARTICLES_RECEIVED,
        json
    }
}

export function subscriptionTabSelected(articles) {
    return {
        type: SUBSCRIPTION_TAB_SELECTED,
        articles
    }
}