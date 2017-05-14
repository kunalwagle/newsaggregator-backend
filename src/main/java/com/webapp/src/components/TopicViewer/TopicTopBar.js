/**
 * Created by kunalwagle on 18/04/2017.
 */
import {PageHeader, OverlayTrigger} from "react-bootstrap";
import React from "react";
import LoginModal from "../LoginModal";

export const TopicTopBar = ({label, loggedIn, isSubscribed, topicId, handleLoginClicked, handleSubscribeClicked}) => {

    const loginPopover = (handleLoginClicked, action) => {
        return (
            <LoginModal handleLoginClicked={handleLoginClicked} action={action}/>
        );
    };

    const button = () => {
        let buttonText = "Subscribe";
        if (!loggedIn) {
            return (
                <OverlayTrigger trigger="click" rootClose placement="bottom" container={this}
                                overlay={loginPopover(handleLoginClicked, (email) => handleSubscribeClicked(topicId, email))}>
                    <button
                        className="col-md-2 col-lg-2 col-sm-12 col-xs-12 pull-right primary-button">{buttonText}</button>
                </OverlayTrigger>
            )
        } else if (!isSubscribed) {
            return (
                <button className="col-md-2 col-lg-2 col-sm-12 col-xs-12 pull-right primary-button"
                        onClick={() => handleSubscribeClicked(topicId)}>{buttonText}</button>
            )
        }
        return (
            <button className="col-md-2 col-lg-2 col-sm-12 col-xs-12 pull-right danger-button">Unsubscribe</button>
        )

    };

    return (
        <div style={{"verticalAlign": "text-bottom"}}>
            <h1 className="col-md-9 col-lg-9 col-sm-12 col-xs-12 whited">{label}</h1>
            {button()}
        </div>
    );
};
