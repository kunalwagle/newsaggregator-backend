/**
 * Created by kunalwagle on 13/05/2017.
 */
import React, {Component} from "react";

export default class SentenceSourcesComponent extends Component {

    generateImageSource() {
        return "/outlets/" + this.props.source + ".png";
    }

    render() {
        return (
            <div>
                <div className="col-lg-3 col-md-3 col-sm-3 col-xs-3">
                    <img src={this.generateImageSource()} className="outlet-icon"/>
                </div>
                <div className="col-lg-7 col-md-7 col-sm-7 col-xs-7 margin-below">
                    <b>{this.props.source}</b>
                    <br/>
                    <p>{this.props.sentence}</p>
                </div>
            </div>
        )
    }

}