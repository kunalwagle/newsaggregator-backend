/**
 * Created by kunalwagle on 18/04/2017.
 */
import {PageHeader, Button} from "react-bootstrap";
import React from "react";

export const TopicTopBar = ({label, loggedIn, handleSubscribeClicked}) => {

    let buttonText = "Subscribe";
    if (!loggedIn) {
        buttonText = "Log in to subscribe";
    }

    return (
        <div>
            <PageHeader bsClass="col-md-9">{label}</PageHeader>
            <Button disabled={!loggedIn} onClick={handleSubscribeClicked} bsStyle="success"
                    bsSize="large">{buttonText}</Button>
        </div>
    );
};
