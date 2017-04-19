/**
 * Created by kunalwagle on 18/04/2017.
 */
import {PageHeader, Button} from "react-bootstrap";
import React from "react";

export const TopicTopBar = ({label, handleSubscribeClicked}) => (
    <div>
        <PageHeader bsClass="col-md-9">{label}</PageHeader>
        <Button disabled={true} onClick={handleSubscribeClicked} bsStyle="success" bsSize="large">Subscribe</Button>
    </div>
);
