/**
 * Created by kunalwagle on 18/04/2017.
 */
import {PageHeader, OverlayTrigger} from "react-bootstrap";
import React, {Component} from "react";

export default class HomeTopBar extends Component {

    render() {
        return (
            <div style={{"verticalAlign": "text-bottom"}}>
                <h3 className="col-md-9 col-lg-9 col-sm-12 col-xs-12 whited">{this.props.title}</h3>
            </div>
        );
    }
};
