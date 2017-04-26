/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";

export const ArticlesSummarised = ({article, fetchInProgressCalled}) => {

    if (!fetchInProgressCalled || article == null || article.articles[0] == null) {
        return (<div></div>);
    }

    const articleInfo = article.articles.map((art, index) => {
        return (
            <div key={index}>
                <b>{art.title}</b>
                <br/>
                <a href={art.articleUrl}>Original Article</a>
                <br/><br/><br/>
            </div>
        )
    });

    return (
        <div>
            <b>Articles Summarised From:</b>
            <br/><br/><br/>
            {articleInfo}
        </div>
    )
};