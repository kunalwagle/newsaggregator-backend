/**
 * Created by kunalwagle on 20/04/2017.
 */
export const EMAIL_CHANGED = 'EMAIL_CHANGED';
export const LOG_IN_CHANGE = 'LOG_IN_CHANGE';

export function emailAddressChanged(text) {
    return {
        type: EMAIL_CHANGED,
        text
    };
}

export function showModal(loggedIn) {
    return {
        type: LOG_IN_CHANGE,
        loggedIn
    };
}
