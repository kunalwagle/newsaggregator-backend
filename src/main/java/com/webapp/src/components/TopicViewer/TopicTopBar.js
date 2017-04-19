/**
 * Created by kunalwagle on 18/04/2017.
 */
import {PageHeader, Button} from "react-bootstrap";
import React from "react";

export const TopicTopBar = ({label, handleSubscribeClicked}) => (
    <div>
        <PageHeader md={9}>{label}</PageHeader>
        <Button enabled={false} onClick={handleSubscribeClicked} bsStyle="success" bsSize="large">Subscribe</Button>
    </div>
);
