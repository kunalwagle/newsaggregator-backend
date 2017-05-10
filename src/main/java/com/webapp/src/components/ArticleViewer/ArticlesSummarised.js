/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import {Checkbox} from "react-bootstrap";
import {getColour} from "../../UtilityMethods";

export const ArticlesSummarised = ({article, annotations, fetchInProgressCalled, mediaType, handleAnnotationSwitch}) => {

    if (!fetchInProgressCalled || article == null || article.articles[0] == null) {
        return (<div></div>);
    }

    const articleInfo = article.articles.map((art, index) => {
        const imageSource = "/outlets/" + art.source + ".png";
        if (!annotations) {
            return (
                <div key={index}>
                    <div>
                        <div className="col-lg-3 col-md-3 col-sm-3 col-xs-3">
                            <img src={imageSource} className="outlet-icon"/>
                        </div>
                        <div className="col-lg-9 col-md-9 col-sm-9 col-xs-9">
                            <b>{art.title}</b>
                            <br/>
                            <a href={art.articleUrl}>Original Article</a>
                        </div>
                    </div>
                    <br/><br/><br/>
                </div>
            )
        } else {
            return (
                <div key={index}>
                    <hr style={{"background": getColour(art.source), "height": "8px"}}/>
                    <img src={imageSource} className="outlet-icon"/>
                    <b>{art.title}</b>
                    <br/>
                    <a href={art.articleUrl}>Original Article</a>
                    <br/><br/><br/>
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