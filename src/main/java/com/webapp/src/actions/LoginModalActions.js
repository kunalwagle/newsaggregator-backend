/**
 * Created by kunalwagle on 20/04/2017.
 */
export const FACEBOOK_LOGIN = 'FACEBOOK_LOGIN';
export const GOOGLE_SUCCESS = 'GOOGLE_SUCCESS';
export const GOOGLE_FAILURE = 'GOOGLE_FAILURE';
export const HIDE_MODAL = 'HIDE_MODAL';
export const SHOW_MODAL = 'SHOW_MODAL';

export function hideModal() {
    return {
        type: HIDE_MODAL
    };
}

export function showModal() {
    return {
        type: SHOW_MODAL
    };
}

export function facebookLogin(response) {
    return {
        type: FACEBOOK_LOGIN,
        response
    };
}

export function googleSuccess(response) {
    return {
        type: GOOGLE_SUCCESS,
        response
    };
}

export function googleFailure(response) {
    return {
        type: GOOGLE_FAILURE,
        response
    };
}
