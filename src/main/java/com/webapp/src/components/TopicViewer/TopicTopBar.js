/**
 * Created by kunalwagle on 18/04/2017.
 */
import {PageHeader, Button} from "react-bootstrap";
import React from "react";

export const TopicTopBar = ({title}) => (
    <div>
        <PageHeader md={9}>{title}</PageHeader>
        <Button bsStyle="success" bsSize="large">Subscribe</Button>
    </div>
);
