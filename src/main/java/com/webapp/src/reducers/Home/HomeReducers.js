/**
 * Created by kunalwagle on 17/05/2017.
 */
import {HOME_ARTICLES, HOME_CLICKED, HOME_STARTED} from "../../actions/Home/HomeActions";

const initialState = {
    articles: [],
    fetchInProgress: false,
    fetchInProgressCalled: false
};

export default function home(state, action) {

    if (typeof state === 'undefined') {
        return initialState;
    }

    switch (action.type) {
        case HOME_STARTED:
            return Object.assign({}, state, {
                fetchInProgress: true,
                fetchInProgressCalled: true
            });
        case HOME_ARTICLES:
            return Object.assign({}, state, {
                fetchInProgress: false,
                fetchInProgressCalled: true,
                articles: action.json
            });
        case HOME_CLICKED:
            return initialState;

    }

    return state;

}