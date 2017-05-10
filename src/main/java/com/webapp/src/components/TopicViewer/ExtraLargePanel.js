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
            <div className="panel-extra-large" onClick={this.props.onClick}
                 style={{"backgroundImage": "url(" + this.getImage() + ")"}}>
                <h5 className="panel-text-large">{this.props.title}</h5>
            </div>
        )
    }

}