/**
 * Created by kunalwagle on 19/04/2017.
 */
import {ARTICLE_CLICKED} from "../../actions/TopicViewer/TopicViewerActions";

const initialState = {
    label: "",
    fetchInProgress: false,
    articles: [],
    selectedArticle: {}
};

export default function topicViewer(state, action) {
    if (typeof state === 'undefined') {
        return initialState;
    }

    switch (action.type) {
        case ARTICLE_CLICKED:
            return Object.assign({}, state, {
                selectedArticle: state.articles[action.index]
            });
    }
}