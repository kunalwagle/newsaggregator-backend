/**
 * Created by kunalwagle on 08/05/2017.
 */
import React, {Component} from "react";
import {Image} from "react-bootstrap";

export default class ExtraLargePanel extends Component {

    getImage = function () {
        if (this.props.image !== "thumbnail") {
            return this.props.image;
        } else {
            return "http://support.yumpu.com/en/wp-content/themes/qaengine/img/default-thumbnail.jpg";
        }
    };

    render() {

        return (
            <div className="panel-extra-large">
                <img src={this.getImage()} className="panel-large-image-container"/>
                <h5>{this.props.title}</h5>
            </div>
        )
    }

}