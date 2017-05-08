/**
 * Created by kunalwagle on 05/05/2017.
 */
import React, {Component} from "react";
import {Image} from "react-bootstrap";

export default class Panel extends Component {

    getImage = function () {
        if (this.props.image !== "thumbnail") {
            return this.props.image;
        } else {
            return "http://support.yumpu.com/en/wp-content/themes/qaengine/img/default-thumbnail.jpg";
        }
    };

    smallText = function () {
        if (this.props.largePanel) {
            return (
                <div>
                    <h6>{this.props.text}</h6>
                </div>
            )
        } else {
            return (<div></div>)
        }
    };

    render() {

        let panelOverallClass = "panel-small";

        if (this.props.largePanel) {
            panelOverallClass = "panel-large";
        }

        return (
            <div className={panelOverallClass} style={{"backgroundImage": "url(" + this.getImage() + ")"}}
                 onClick={this.props.onClick}>
                <div className="panel-text-small panel-text-small-background">
                    <h5>{this.props.title}</h5>
                    {this.smallText()}
                </div>
            </div>
        )
    }

}