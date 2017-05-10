/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import {PageHeader, Image} from "react-bootstrap";
import {pluck, isEmpty, isEqual} from "underscore";
import {getColour} from "../../UtilityMethods";

export const ArticleContent = ({article, sources, topicId, annotations, fetchInProgressCalled, handleReloadNeeded}) => {

    if (!fetchInProgressCalled) {
        handleReloadNeeded(topicId);
        return (
            <div className="loader"></div>
        )
    }

    const getSummary = () => {
        for (let key in article.summaryMap) {
            if (!article.summaryMap.hasOwnProperty(key)) continue;
            if ("[" + sources.sort().toString() + "]" === key) {
                return article.summaryMap[key];
            }
        }
    };

    const stripIntoSentences = (summary) => pluck(summary, "sentence");

    const paragraphsPlain = (node) => {
        return node.map((text, index) => <p key={index}>{text}</p>)
            .reduce((nodes, node) => nodes.concat(node), []);
    };

    const paragraphsAnnotated = (node) => {
        return node.map((text, index) => {
            if (text.relatedNodes == undefined) {
                return (
                    <p key={index} style={{"backgroundColor": getColour(text.source)}}>
                        {text.sentence}
                    </p>
                )
            } else if (text.relatedNodes.length === 0) {
                return (
                    <p key={index} style={{"backgroundColor": getColour(text.source)}}>
                        {text.sentence}
                    </p>
                )
            } else {
                return (
                    <p key={index}>{text.sentence}</p>
                )
            }
        })
            .reduce((nodes, node) => nodes.concat(node), []);
    };

    const paragraph = () => {
        if (!annotations) {
            return (<div className="image-width">{paragraphsPlain(stripIntoSentences(getSummary()))}</div>)
        } else {
            return (<div className="image-width">{paragraphsAnnotated(getSummary())}</div>)
        }
    };

    return (
        <div className="white">
            <h1>
                {article.articles[0].title}
            </h1>
            <img src={article.articles[0].imageUrl} className="image-width"/>
            <br/><br/>
            {paragraph()}
        </div>
    )

};