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
            <div className="loader">Loading...</div>
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
            return (<h5>{paragraphsPlain(stripIntoSentences)}</h5>)
        } else {
            return (<h5>{paragraphsAnnotated(article.summary[0])}</h5>)
        }
    };

    return (
        <div>
            <h1>
                {article.articles[0].title}
            </h1>
            <Image width={750} src={article.articles[0].imageUrl} rounded/>
            {paragraph()}
        </div>
    )

};