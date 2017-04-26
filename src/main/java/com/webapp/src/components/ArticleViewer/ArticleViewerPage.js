/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import ArticleContent from "../../containers/ArticleViewer/ArticleContent";
import ArticlesSummariseContainer from "../../containers/ArticleViewer/ArticleSummarisedContainer";
import {Grid, Row, Col} from "react-bootstrap";

export const ArticleViewerPage = ({params}) => (
    <Grid>
        <Row>
            <Col md={9}>
                <ArticleContent topicId={params.topicId} articleId={params.articleId}/>
            </Col>
            <Col md={3}>
                <ArticlesSummariseContainer topicId={params.topicId} articleId={params.articleId}/>
            </Col>
        </Row>
    </Grid>
);