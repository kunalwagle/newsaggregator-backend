/**
 * Created by kunalwagle on 15/05/2017.
 */
import {OUTLET_CHANGE, DIGEST_CHANGE, CHANGE_TOPIC} from "../../actions/Settings/SettingsActions";
import {contains, reject} from "underscore";

const initialState = {
    digest: false,
    chosenOutlets: [],
    activeIndex: 0,
    topicName: "",
    topicId: ""

};

export default function searchResults(state, action) {
    if (typeof state === 'undefined') {
        return initialState;
    }

    switch (action.type) {
        case OUTLET_CHANGE: {
            let newOutlets = state.chosenOutlets.slice(0);
            if (contains(newOutlets, action.outlet)) {
                newOutlets = reject(newOutlets, (out) => out === action.outlet);
            } else {
                newOutlets.push(action.outlet);
            }
            return Object.assign({}, state, {
                chosenOutlets: newOutlets
            })
        }
        case DIGEST_CHANGE:
            return Object.assign({}, state, {
                digest: action.digest
            });
        case CHANGE_TOPIC:
            return Object.assign({}, state, {
                topicName: action.topic.labelHolder.label,
                topicId: action.topic.labelHolder.id,
                digest: action.topic.digests,
                chosenOutlets: action.topic.sources,
                activeIndex: action.index
            })
    }


    return state;
}