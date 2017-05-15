/**
 * Created by kunalwagle on 15/05/2017.
 */
import {push} from "react-router-redux";

export const DIGEST_CHANGE = "DIGEST_CHANGE";
export const OUTLET_CHANGE = "OUTLET_CHANGE";
export const CHANGE_TOPIC = "CHANGE_TOPIC";
export const INITIALISE = "INITIALISE";

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
        return dispatch(setup(getState().loggedIn.user));
    }
};

export const setup = (user) => {
    return {
        type: INITIALISE,
        topic: user.topics[0]
    }
}