/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import {ArticleContent} from "../../containers/ArticleViewer/ArticleContent";
import {ArticlesSummarised} from "../../containers/ArticleViewer/ArticleSummarisedContainer";

export const ArticleViewerPage = () => (
    <Grid>
        <Row>
            <Col md={9}>
                <ArticleContent/>
            </Col>
            <Col md={3}>
                <ArticlesSummarised/>
            </Col>
        </Row>
    </Grid>
);