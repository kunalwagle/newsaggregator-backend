/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import ArticleContent from "../../containers/ArticleViewer/ArticleContent";
import ArticlesSummariseContainer from "../../containers/ArticleViewer/ArticleSummarisedContainer";
import {Grid, Row, Col} from "react-bootstrap";

export const ArticleViewerPage = ({params}) => (
    <div>
        <div className="row">
            <div
                className="col-md-7 col-lg-7 col-sm-10 col-xs-10 col-lg-offset-1 col-md-offset-1 col-sm-offset-1 col-xs-offset-1">
                <ArticleContent topicId={params.topicId} articleId={params.articleId}/>
            </div>
            <div className="col-md-3 col-lg-3 col-sm-10 col-xs-10 col-sm-offset-1 col-xs-offset-1">
                <ArticlesSummariseContainer topicId={params.topicId} articleId={params.articleId}/>
            </div>
        </div>
    </div>
);