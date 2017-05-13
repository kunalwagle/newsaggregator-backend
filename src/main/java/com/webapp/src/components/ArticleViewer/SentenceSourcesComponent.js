/**
 * Created by kunalwagle on 13/05/2017.
 */
import React, {Component} from "react";
import {Popover} from "react-bootstrap";
import {getPublicationName} from "../../UtilityMethods";

class SentenceComponent extends Component {

    generateImageSource() {
        return "/outlets/" + this.props.source + ".png";
    }

    render() {
        return (
            <div className="row popover-cell">
                <div className="col-lg-3 col-md-3 col-sm-3 col-xs-3">
                    <img src={this.generateImageSource()} className="outlet-icon"/>
                </div>
                <div className="col-lg-8 col-md-8 col-sm-8 col-xs-8 margin-below">
                    <div className="row">
                        <b>{getPublicationName(this.props.source)}</b>
                    </div>
                    <div className="row">
                        <p>{this.props.sentence}</p>
                    </div>
                </div>
            </div>
        )
    }

}

export default class SentenceSourcesComponent extends Component {

    render() {
        return (
            <Popover id="sentencePopover" title="Sentence Sources" {...this.props} bsClass="popover">
                {
                    this.props.sentences.map((sentence, index) => {
                        return (
                            <SentenceComponent key={index} source={sentence.source} sentence={sentence.sentence}
                                               className="popover-cell"/>
                        )
                    })
                }
            </Popover>
        )
    }

}