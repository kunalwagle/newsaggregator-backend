/**
 * Created by kunalwagle on 19/04/2017.
 */

const initialState = {
    article: {}
};

export default function topicViewer(state, action) {
    if (typeof state === 'undefined') {
        return initialState;
    }

    switch (action.type) {
        default:
            return initialState;
    }
}