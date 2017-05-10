/**
 * Created by kunalwagle on 03/05/2017.
 */
import {ANNOTATIONS_CHANGE, CHECKBOX_CHANGE, DEFAULT_CHECKBOXES} from "../../actions/ArticleViewer/ArticleActions";
import {without} from "underscore";

const initialState = {
    annotations: false,
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
                sources: action.sources
            })

    }

    return state;

}