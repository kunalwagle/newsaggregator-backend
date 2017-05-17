/**
 * Created by kunalwagle on 17/05/2017.
 */
import {push} from "react-router-redux";
import {getIPAddress} from "../../UtilityMethods";

export const DIGEST_CLICKED = 'DIGEST_CLICKED';
export const DIGEST_ARTICLES = 'DIGEST_ARTICLES';
export const DIGEST_STARTED = 'DIGEST_STARTED';

export function digestArticleClicked(topicId, articleId) {
    return (dispatch) => {

        dispatch(push("/topic/" + topicId + "/article/" + articleId));
        return {
            type: DIGEST_CLICKED
        }
    }
}

export function reloadDigest(digestId) {
    return (dispatch) => {
        dispatch(digestReloadStarted());
        return fetch(getIPAddress() + "digest/" + digestId)
            .then(response => response.json())
            .then(json => dispatch(gotArticles(json)))
    }
}

export function digestReloadStarted() {
    return {
        type: DIGEST_STARTED
    }
}

export function gotArticles(json) {
    return {
        type: DIGEST_ARTICLES,
        json
    }
}

