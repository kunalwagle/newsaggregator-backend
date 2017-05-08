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
        <div style={{"verticalAlign": "text-bottom"}}>
            <h1 className="col-md-9 col-lg-9 col-sm-12 col-xs-12 whited">{label}</h1>
            <button className="col-md-2 col-lg-2 col-sm-12 col-xs-12 pull-right primary-button" disabled={!loggedIn}
                    onClick={() => handleSubscribeClicked(label)}>{buttonText}</button>
        </div>
    );
};
