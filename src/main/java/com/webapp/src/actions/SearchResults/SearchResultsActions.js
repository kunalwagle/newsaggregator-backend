/**
 * Created by kunalwagle on 19/04/2017.
 */
import fetch from "isomorphic-fetch";
import {push} from "react-router-redux";
import {getIPAddress} from "../../UtilityMethods";

export const VIEW_START = 'VIEW_START';
export const ARTICLES_RECEIVED = 'ARTICLES_RECEIVED';
export const SUBSCRIPTION_TAB_SELECTED = 'SUBSCRIPTION_TAB_SELECTED';
export const NOTHING = 'NOTHING';


export function viewClicked(title, topic, page, articleCount) {
    return (dispatch, getState) => {
        dispatch(viewStarted());
        if (title == undefined) {
            dispatch(push("/topic/" + title));
        }
        if (getState().loggedIn.loggedIn) {
            title = title + "/user/" + getState().loggedIn.email + "/" + page;
        } else {
            title = title + "/" + page;
        }
        return fetch(getIPAddress() + "topic/" + title)
            .then(response => response.json())
            .then(json => dispatch(articlesReceived(json, page, articleCount)))
    }
}

export function changePage(title, topic, page) {
    return (dispatch, getState) => {
        dispatch(viewStarted());
        if (getState().loggedIn.loggedIn) {
            title = title + "/user/" + getState().loggedIn.email + "/" + page;
        } else {
            title = title + "/" + page;
        }
        return fetch(getIPAddress() + "topic/" + title)
            .then(response => response.json())
            .then(json => dispatch(articlesReceived(json, page)))
    }
}

export function articleClicked(title) {
    return (dispatch, getState) => {
        dispatch(viewStarted());
        if (getState().loggedIn.loggedIn) {
            title = title + "/user/" + getState().loggedIn.email;
        }
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

export function articlesReceived(json, page, articleCount) {
    return {
        type: ARTICLES_RECEIVED,
        json,
        page,
        articleCount
    }
}

export function subscriptionTabSelected(index) {
    return {
        type: SUBSCRIPTION_TAB_SELECTED,
        index
    }
}

export function nothing() {
    return {
        type: NOTHING
    }
}

export function changeTab(index) {
    return {
        type: SUBSCRIPTION_TAB_SELECTED,
        index
    }
}