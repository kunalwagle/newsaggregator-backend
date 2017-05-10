/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import {PageHeader, Image} from "react-bootstrap";
import {pluck, isEmpty} from "underscore";
import {getColour} from "../../UtilityMethods";

export const ArticleContent = ({article, topicId, annotations, fetchInProgressCalled, handleReloadNeeded}) => {

    if (!fetchInProgressCalled) {
        handleReloadNeeded(topicId);
        return (
            <div className="loader"></div>
        )
    }

    const stripIntoSentences = pluck(article.summary[0], "sentence");

    const paragraphsPlain = (node) => {
        return node.map((text, index) => <p key={index}>{text}</p>)
            .reduce((nodes, node) => nodes.concat(node), []);
    };

    const paragraphsAnnotated = (node) => {
        return node.map((text, index) => {
            if (text.relatedNodes == undefined || text.relatedNodes.length === 0) {
                return (
                    <p key={index} style={{"backgroundColor": getColour(text.source)}}>
                        {text.sentence}
                    </p>
                )
            }
        })
            .reduce((nodes, node) => nodes.concat(node), []);
    };

    const paragraph = () => {
        if (!annotations) {
            return (<div className="image-width">{paragraphsPlain(stripIntoSentences)}</div>)
        } else {
            return (<div className="image-width">{paragraphsAnnotated(article.summary[0])}</div>)
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