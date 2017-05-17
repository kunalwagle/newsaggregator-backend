/**
 * Created by kunalwagle on 18/04/2017.
 */
import {PageHeader, OverlayTrigger} from "react-bootstrap";
import React from "react";

export const DigestTopBar = ({label}) => {

    return (
        <div style={{"verticalAlign": "text-bottom"}}>
            <h1 className="col-md-9 col-lg-9 col-sm-12 col-xs-12 whited">{label}</h1>
        </div>
    );
};
