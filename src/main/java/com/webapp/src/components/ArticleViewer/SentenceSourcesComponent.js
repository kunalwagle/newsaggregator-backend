/**
 * Created by kunalwagle on 13/05/2017.
 */
import React, {Component} from "react";
import {Popover} from "react-bootstrap";

class SentenceComponent extends Component {

    generateImageSource() {
        return "/outlets/" + this.props.source + ".png";
    }

    render() {
        return (
            <div className="row">
                <div className="col-lg-3 col-md-3 col-sm-3 col-xs-3">
                    <img src={this.generateImageSource()} className="outlet-icon"/>
                </div>
                <div className="col-lg-7 col-md-7 col-sm-7 col-xs-7 margin-below">
                    <div className="row">
                        <b>{this.props.source}</b>
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
            <Popover id="sentencePopover" title="Sentence Sources">
                {
                    this.props.sentences.map((sentence, index) => {
                        return (
                            <SentenceComponent key={index} source={sentence.source} sentence={sentence.sentence}/>
                        )
                    })
                }
            </Popover>
        )
    }

}