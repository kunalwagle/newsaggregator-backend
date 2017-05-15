/**
 * Created by kunalwagle on 15/05/2017.
 */
import {OUTLET_CHANGE, DIGEST_CHANGE} from "../../actions/Settings/SettingsActions";
import {contains, reject} from "underscore";

const initialState = {
    digest: false,
    chosenOutlets: []
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
            })
    }


    return state;
}