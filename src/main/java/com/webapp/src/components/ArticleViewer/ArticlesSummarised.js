/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import {Checkbox} from "react-bootstrap";
import {getColour} from "../../UtilityMethods";
import {contains} from "underscore";

export const ArticlesSummarised = ({article, sources, annotations, fetchInProgressCalled, mediaType, handleAnnotationSwitch, handleDefaultCheckboxes, handleCheckboxChange}) => {

    if (!fetchInProgressCalled || article == null || article.articles[0] == null) {
        return (<div></div>);
    }

    if (sources.length === 0) {
        handleDefaultCheckboxes(article);
        return (<div></div>);
    }

    const articleImage = (imageSource, source) => (
        <div className="row">
            <div className="col-md-12 col-lg-12 col-xs-12 col-sm-12">
                <img src={imageSource} alt={imageSource.substring(0, imageSource.length - 3) + "jpg"}
                     className="outlet-icon"/>
            </div>
            <div className="col-md-12 col-lg-12 col-xs-12 col-sm-12">
                <hr style={{"background": getColour(source), "height": "20px"}} className="no-border"/>
            </div>
        </div>
    );

    const articleInfo = article.articles.map((art, index) => {
        const imageSource = "/outlets/" + art.source + ".png";
        if (!annotations) {
            return (
                <div key={index} className="row">
                    <div >
                        <div className="col-lg-2 col-md-2 col-sm-2 col-xs-2">
                            <img src={imageSource} className="outlet-icon"/>
                        </div>
                        <div className="col-lg-6 col-md-6 col-sm-6 col-xs-6 margin-below">
                            <b>{art.title}</b>
                            <br/>
                            <a href={art.articleUrl}>Original Article</a>
                        </div>
                        <div className="col-lg-4 col-md-4 col-sm-4 col-xs-4 margin-below">
                            <label className="switch">
                                <input type="checkbox" checked={contains(sources, art.source)}
                                       onChange={event => handleCheckboxChange(event, art.source)}/>
                                <div className="slider round"></div>
                            </label>
                        </div>
                    </div>

                </div>
            )
        } else {
            return (
                <div key={index} className="row">
                    <div>
                        <div className="col-lg-2 col-md-2 col-sm-2 col-xs-2">
                            {articleImage(imageSource, art.source)}
                        </div>
                        <div className="col-lg-6 col-md-6 col-sm-6 col-xs-6 margin-below">
                            <b>{art.title}</b>
                            <br/>
                            <a href={art.articleUrl}>Original Article</a>
                        </div>
                        <div className="col-lg-4 col-md-4 col-sm-4 col-xs-4 margin-below">
                            <label className="switch">
                                <input type="checkbox" checked={contains(sources, art.source)}
                                       onChange={event => handleCheckboxChange(event, art.source)}/>
                                <div className="slider round"></div>
                            </label>
                        </div>
                    </div>
                    <br/><br/><br/><br/>
                </div>
            )
        }
    });

    let className = "white ";
    if (mediaType !== "large" && mediaType !== "infinity") {
        className = "white horizontalLine";
    } else {
        className = "white verticalLine";
    }

    return (
        <div className={className}>
            <Checkbox checked={annotations} onClick={() => handleAnnotationSwitch(annotations)}>
                Show summary annotations
            </Checkbox>
            <br/><br/><br/>
            <b>Articles Summarised From:</b>
            <br/><br/><br/>
            {articleInfo}
        </div>
    )
};