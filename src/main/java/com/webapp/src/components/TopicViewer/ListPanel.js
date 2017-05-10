/**
 * Created by kunalwagle on 08/05/2017.
 */
import React, {Component} from "react";
import {Image} from "react-bootstrap";

export default class ListPanel extends Component {

    getImage = function () {
        if (this.props.image !== "thumbnail") {
            return this.props.image;
        } else {
            return "http://support.yumpu.com/en/wp-content/themes/qaengine/img/default-thumbnail.jpg";
        }
    };

    render() {

        return (
            <div className="list-cell" onClick={this.props.onClick}>
                <div className="col-xs-3 pull-left">
                    <img src={this.getImage()} className="list-image"/>
                </div>
                <div className="col-xs-9 pull-right">
                    <h5>{this.props.title}</h5>
                </div>
            </div>
        )
    }

}