/**
 * Created by kunalwagle on 19/04/2017.
 */
import fetch from "isomorphic-fetch";

export const VIEW_START = 'VIEW_START';
export const ARTICLES_RECEIVED = 'ARTICLES_RECEIVED';

export function viewClicked(title) {
    return (dispatch) => {
        dispatch(viewStarted(title));
        return fetch("http://localhost:8182/api/topic/" + title)
            .then(response => response.json())
            .then(json => dispatch(articlesReceived(json)))
    }
}

export function viewStarted(title) {
    return {
        type: VIEW_START,
        label: title
    }
}

export function articlesReceived(json) {
    return {
        type: ARTICLES_RECEIVED,
        json
    }
}