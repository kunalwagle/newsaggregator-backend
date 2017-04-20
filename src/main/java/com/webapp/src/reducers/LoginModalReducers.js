/**
 * Created by kunalwagle on 20/04/2017.
 */
import {FACEBOOK_LOGIN, GOOGLE_SUCCESS, HIDE_MODAL, SHOW_MODAL} from "../actions/LoginModalActions";

const initialState = {
    loggedIn: false,
    pictureSource: "",
    email: "",
    name: "",
    modalShown: false
};

export default function loggedIn(state, action) {
    if (typeof state === 'undefined') {
        return initialState;
    }

    switch (action.type) {
        case HIDE_MODAL:
            return Object.assign({}, state, {
                modalShown: false
            });
        case SHOW_MODAL:
            return Object.assign({}, state, {
                modalShown: true
            });
        case FACEBOOK_LOGIN: {
            if (action.response != null) {
                if (action.response.status === "connected") {
                    return Object.assign({}, state, {
                        loggedIn: true,
                        modalShown: false
                    })
                }
            }
            return state;
        }
        case GOOGLE_SUCCESS:
            return Object.assign({}, state, {
                loggedIn: true,
                modalShown: false,
                email: action.response.profileObj.getBasicProfile().getEmail(),
                name: action.response.profileObj.getBasicProfile().getName(),
                pictureSource: action.response.profileObj.getBasicProfile().getImageUrl()
            });
        default:
            return state
    }

}