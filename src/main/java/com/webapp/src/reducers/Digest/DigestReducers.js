/**
 * Created by kunalwagle on 17/05/2017.
 */
import {DIGEST_ARTICLES, DIGEST_STARTED, DIGEST_CLICKED} from "../../actions/Digest/DigestActions";

const initialState = {
    articles: [],
    fetchInProgress: false,
    fetchInProgressCalled: false
};

export default function digest(state, action) {

    if (typeof state === 'undefined') {
        return initialState;
    }

    switch (action.type) {
        case DIGEST_STARTED:
            return Object.assign({}, state, {
                fetchInProgress: true,
                fetchInProgressCalled: true
            });
        case DIGEST_ARTICLES:
            return Object.assign({}, state, {
                fetchInProgress: false,
                fetchInProgressCalled: true,
                articles: JSON.parse(action.json.articleHolders)
            });
        case DIGEST_CLICKED:
            return initialState;

    }

    return initialState;

}