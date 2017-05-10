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
        return node.map((text, index) => <p key={index}><u style={{"color": getColour(text.source)}}>{text.sentence}</u>
        </p>)
            .reduce((nodes, node) => nodes.concat(node), []);
    };

    const paragraph = () => {
        if (!annotations) {
            return (<p className="align-with-image">{paragraphsPlain(stripIntoSentences)}</p>)
        } else {
            return (<p className="align-with-image">{paragraphsAnnotated(article.summary[0])}</p>)
        }
    };

    return (
        <div className="white">
            <h1>
                {article.articles[0].title}
            </h1>
            <img width={750} src={article.articles[0].imageUrl}/>
            <br/><br/>
            {paragraph()}
        </div>
    )

};