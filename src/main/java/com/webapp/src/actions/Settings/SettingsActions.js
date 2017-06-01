/**
 * Created by kunalwagle on 15/05/2017.
 */
import {push} from "react-router-redux";
import {getIPAddress} from "../../UtilityMethods";
import {login} from "../LoginModalActions";
import {toastr} from "react-redux-toastr";

export const DIGEST_CHANGE = "DIGEST_CHANGE";
export const OUTLET_CHANGE = "OUTLET_CHANGE";
export const CHANGE_TOPIC = "CHANGE_TOPIC";
export const INITIALISE = "INITIALISE";
export const SAVE = "SAVE";

export const digestChange = (digest) => {
    return {
        type: DIGEST_CHANGE,
        digest: !digest
    }
};

export const outletChange = (outlet) => {
    return {
        type: OUTLET_CHANGE,
        outlet
    }
};

export const changeTopic = (topic, index) => {
    return {
        type: CHANGE_TOPIC,
        topic,
        index
    }
};

export const initialise = () => {
    return (dispatch, getState) => {
        dispatch(push("/settings"));
        if (getState().loggedIn.loggedIn) {
            return dispatch(login(getState().loggedIn.email, setup));
        }
    }
};

export const setup = (user) => {
    return (dispatch, getState) => {
        if (user == undefined) {
            user = getState().loggedIn.user;
        }
        return {
            type: INITIALISE,
            topic: user.topics[0]
        }
    }
};

export const save = (topicId) => {
    return (dispatch, getState) => {
        const value = {
            "sources": getState().settings.chosenOutlets,
            "digest": getState().settings.digest,
            "user": getState().loggedIn.user.emailAddress,
            "topicId": topicId
        };
        const data = JSON.stringify(value);
        return fetch(getIPAddress() + "settings", {
            method: "POST",
            body: data
        })
            .then(response => dispatch(saveComplete()));
    }
};

export const saveComplete = () => {
    toastr.success("Your settings have been saved");
    return {
        type: SAVE
    }
};