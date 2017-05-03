/**
 * Created by kunalwagle on 03/05/2017.
 */
import {ANNOTATIONS_CHANGE} from "../../actions/ArticleViewer/ArticleActions";

const initialState = {
    annotations: false
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
    }

    return state;

}