/**
 * Created by kunalwagle on 15/05/2017.
 */
import {push} from "react-router-redux";
import {getIPAddress} from "../../UtilityMethods";
import {getSubscriptions, populateSubscriptions} from "../LoginModalActions";
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
            dispatch(getSubscriptions(true));
            return dispatch(setup());
        }
    }
};

export const setup = (topics) => {
    return (dispatch, getState) => {
        if (topics == undefined) {
            topics = getState().loggedIn.topics;
        }
        return {
            type: INITIALISE,
            topic: topics[0]
        }
    }
};

export const save = (topicId) => {
    return (dispatch, getState) => {
        let value = {
            "sources": getState().settings.chosenOutlets,
            "digest": getState().settings.digest,
            "user": getState().loggedIn.email
        };
        if (topicId !== undefined) {
            value.topicId = topicId;
        }
        const data = JSON.stringify(value);
        return fetch(getIPAddress() + "settings", {
            method: "POST",
            body: data
        })
            .then(response => response.json())
            .then(json => dispatch(populateSubscriptions(json)))
            .then(dispatch(saveComplete()));
    }
};

export const saveComplete = () => {
    toastr.success("Your settings have been saved");
    return {
        type: SAVE
    }
};