/**
 * Created by kunalwagle on 03/05/2017.
 */
import {map, uniq, contains} from "underscore";
import {getIPAddress} from "../../UtilityMethods";

export const ANNOTATIONS_CHANGE = "ANNOTATIONS_CHANGE";
export const CHECKBOX_CHANGE = "CHECKBOX_CHANGE";
export const DEFAULT_CHECKBOXES = "DEFAULT_CHECKBOXES";
export const RECEIVED_ARTICLE = "RECEIVED_ARTICLE";
export const FETCH_IN_PROGRESS_TOGGLE = "FETCH_IN_PROGRESS_TOGGLE";

export function annotationsChange(annotations) {
    return {
        type: ANNOTATIONS_CHANGE,
        annotations: !annotations
    }
}

export function checkboxChange(on, source) {
    return {
        type: CHECKBOX_CHANGE,
        source,
        on: on
    }
}

export function defaultCheckboxes(article) {
    return (dispatch, getState) => {
        const defaultSources = getState().searchResults.defaultCheckbox;

        let sources = map(article.articles, (art) => {
            return art.source;
        });

        const finalSources = [];

        for (let i = 0; i < sources.length; i++) {
            if (contains(defaultSources, sources[i])) {
                finalSources.push(sources[i]);
            }
        }

        return dispatch(checkboxes(finalSources))
    }
}

export function checkboxes(finalSources) {
    return {
        type: DEFAULT_CHECKBOXES,
        finalSources
    }
}

export function reloadArticle(topicId, articleId) {
    return (dispatch, getState) => {
        let title = "article/" + articleId + "/topic/" + topicId;
        if (getState().loggedIn.loggedIn) {
            title = title + "/user/" + getState().loggedIn.email;
        } else {
            title = title + "/user/none";
        }
        return fetch(getIPAddress() + title)
            .then(response => response.json())
            .then(json => dispatch(articleReceived(json)))
    }
}

export function articleReceived(json) {
    return {
        type: RECEIVED_ARTICLE,
        json
    }
}

export function fetchInProgressToggle() {
    return {
        type: FETCH_IN_PROGRESS_TOGGLE
    }
}


