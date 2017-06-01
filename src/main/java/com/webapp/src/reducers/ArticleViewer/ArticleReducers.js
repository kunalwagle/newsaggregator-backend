/**
 * Created by kunalwagle on 03/05/2017.
 */
import {
    ANNOTATIONS_CHANGE,
    CHECKBOX_CHANGE,
    DEFAULT_CHECKBOXES,
    FETCH_IN_PROGRESS_TOGGLE,
    RECEIVED_ARTICLE
} from "../../actions/ArticleViewer/ArticleActions";
import {without} from "underscore";

const initialState = {
    annotations: false,
    fetchInProgressCalled: false,
    article: {},
    sources: []
};

export default function articleViewer(state, action) {

    if (state === undefined) {
        state = initialState;
    }

    switch (action.type) {
        case ANNOTATIONS_CHANGE:
            return Object.assign({}, state, {
                annotations: action.annotations
            });
        case CHECKBOX_CHANGE: {
            let newSources = state.sources.slice(0);
            if (action.on) {
                newSources.push(action.source);
            } else {
                newSources = without(newSources, action.source);
            }
            return Object.assign({}, state, {
                sources: newSources
            });
        }
        case DEFAULT_CHECKBOXES:
            return Object.assign({}, state, {
                sources: action.finalSources
            });
        case FETCH_IN_PROGRESS_TOGGLE:
            return Object.assign({}, state, {
                fetchInProgressCalled: false
            });
        case RECEIVED_ARTICLE:
            return Object.assign({}, state, {
                sources: action.json.defaultString,
                article: action.json.clusterHolder
            });

    }

    return state;

}