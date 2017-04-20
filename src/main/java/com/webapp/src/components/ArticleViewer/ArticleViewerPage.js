/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import ArticleContent from "../../containers/ArticleViewer/ArticleContent";
import ArticlesSummariseContainer from "../../containers/ArticleViewer/ArticleSummarisedContainer";
import {Grid, Row, Col} from "react-bootstrap";

export const ArticleViewerPage = () => (
    <Grid>
        <Row>
            <Col md={9}>
                <ArticleContent/>
            </Col>
            <Col md={3}>
                <ArticlesSummariseContainer/>
            </Col>
        </Row>
    </Grid>
);