/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import {Checkbox} from "react-bootstrap";
import {getColour} from "../../UtilityMethods";

export const ArticlesSummarised = ({article, annotations, fetchInProgressCalled, handleAnnotationSwitch}) => {

    if (!fetchInProgressCalled || article == null || article.articles[0] == null) {
        return (<div></div>);
    }

    const articleInfo = article.articles.map((art, index) => {
        if (!annotations) {
            return (
                <div key={index}>
                    <b>{art.title}</b>
                    <br/>
                    <a href={art.articleUrl}>Original Article</a>
                    <br/><br/><br/>
                </div>
            )
        } else {
            return (
                <div key={index}>
                    <hr style={{"background": getColour(art.source), "height": "8px"}}/>
                    <b>{art.title}</b>
                    <br/>
                    <a href={art.articleUrl}>Original Article</a>
                    <br/><br/><br/>
                </div>
            )
        }
    });

    return (
        <div className="white">
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