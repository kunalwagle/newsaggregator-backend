/**
 * Created by kunalwagle on 17/05/2017.
 */
import {push} from "react-router-redux";
import {getIPAddress} from "../../UtilityMethods";

export const HOME_CLICKED = 'HOME_CLICKED';
export const HOME_ARTICLES = 'HOME_ARTICLES';
export const HOME_STARTED = 'HOME_STARTED';

export function digestArticleClicked(topicId, articleId) {
    return (dispatch) => {

        dispatch(push("/topic/" + topicId + "/article/" + articleId));
        return {
            type: HOME_CLICKED
        }
    }
}

export function reloadDigest() {
    return (dispatch, getState) => {
        dispatch(digestReloadStarted());
        let addendum = "none";
        if (getState().loggedIn.loggedIn) {
            addendum = getState().loggedIn.email;
        }
        return fetch(getIPAddress() + "home/" + addendum)
            .then(response => response.json())
            .then(json => dispatch(gotArticles(json)))
    }
}

export function digestReloadStarted() {
    return {
        type: HOME_STARTED
    }
}

export function gotArticles(json) {
    return {
        type: HOME_ARTICLES,
        json
    }
}

