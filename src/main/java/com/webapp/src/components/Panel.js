/**
 * Created by kunalwagle on 05/05/2017.
 */
import React, {Component} from "react";
import {Image} from "react-bootstrap";

export default class Panel extends Component {

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
            <div className={panelOverallClass}>
                <img src="https://i.ytimg.com/vi/0AGXi-oKxfw/maxresdefault.jpg" className="panel-image-container"/>
                <h5>{this.props.title}</h5>
                {this.smallText()}
            </div>
        )
    }

}