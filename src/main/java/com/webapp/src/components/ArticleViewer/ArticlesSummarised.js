/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";

export const ArticlesSummarised = ({article}) => {

    const articleInfo = article.articles.map((art) => {
        return (
            <div>
                <b>{art.title}</b>
                <a href={art.articleUrl}>Original Article</a>
                <br/><br/><br/>
            </div>
        )
    });

    return (
        {articleInfo}
    )
};