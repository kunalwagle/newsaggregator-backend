/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import {PageHeader, Image} from "react-bootstrap";

export const ArticleContent = ({article}) => {

    const paragraphs = (node) => {
        if (fetchInProgress) return "Summarising. Results will be returned shortly";
        if (node === undefined) return "Summary will appear here";
        return node.split('\n').map(text => <p>{text}</p>)
            .reduce((nodes, node) => nodes.concat(node), []);
    };

    return (
        <div>
            <PageHeader>
                {article.articles[0].title}
            </PageHeader>
            <br/><br/><br/>
            <Image src={article.articles[0].imageUrl} rounded/>
            <br/><br/><br/>
            <h5>{paragraphs(article.summary)}</h5>
        </div>
    )

};