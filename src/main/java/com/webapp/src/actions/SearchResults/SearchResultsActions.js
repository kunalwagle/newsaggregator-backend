/**
 * Created by kunalwagle on 19/04/2017.
 */
import fetch from "isomorphic-fetch";

export const VIEW_START = 'VIEW_START';
export const ARTICLES_RECEIVED = 'ARTICLES_RECEIVED';

export function viewClicked(title) {
    return (dispatch) => {
        dispatch(viewStarted());
        return fetch("localhost:3000/api/topic/" + title)
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