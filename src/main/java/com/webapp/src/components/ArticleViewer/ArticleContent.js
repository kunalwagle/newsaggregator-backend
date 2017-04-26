/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import {PageHeader, Image} from "react-bootstrap";
import {pluck, isEmpty} from "underscore";

export const ArticleContent = ({article, topicId, fetchInProgressCalled, handleReloadNeeded}) => {

    if (!fetchInProgressCalled) {
        handleReloadNeeded(topicId);
        return (
            <div className="loader">Loading...</div>
        )
    }

    const stripIntoSentences = pluck(article.summary[0], "sentence");

    const paragraphs = (node) => {
        return node.map((text, index) => <p key={index}>{text}</p>)
            .reduce((nodes, node) => nodes.concat(node), []);
    };

    return (
        <div>
            <h1>
                {article.articles[0].title}
            </h1>
            <Image width={750} src={article.articles[0].imageUrl} rounded/>
            <h5>{paragraphs(stripIntoSentences)}</h5>
        </div>
    )

};