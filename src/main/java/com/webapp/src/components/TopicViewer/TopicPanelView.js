/**
 * Created by kunalwagle on 18/04/2017.
 */
import {map, filter, size} from "underscore";
import React from "react";
import {Button, Image, Label, Thumbnail, Grid, Row, Col} from "react-bootstrap";
import {Link} from "react-router";


const thumbnails = (row, idx, topicId, handleArticleClick) => {
    return (
        <Row key={idx}>
            {row.map((article, index) => {
                return (
                    <Col md={3} key={index}>
                        <Thumbnail src={article.articles[0].imageUrl}>
                            <h4>{article.articles[0].title}</h4>
                            <Button onClick={(event) => handleArticleClick(event, topicId, article.id)}
                                    bsStyle="default">
                                View
                            </Button>
                        </Thumbnail>
                    </Col>
                )
            })}
        </Row>
    )
};

const TopicPanelView = ({articles, fetchInProgress, fetchInProgressCalled, topicId, handleReloadNeeded, handleArticleClick}) => {

    if (fetchInProgress) {
        return (
            <div className="loader">Loading...</div>
        )
    }

    if (!fetchInProgressCalled) {
        handleReloadNeeded(topicId);
        return (
            <div className="loader">Loading...</div>
        )
    }

    articles = filter(articles, function (article) {
        return size(article.articles) && article.articles[0] != null;
    });

    if (articles.length === 0) {
        return (
            <div>There is no recent news for this topic</div>
        )
    }

    let rows = [];
    for (let i = 0; i < articles.length; i += 4) {
        let temparray = articles.slice(i, i + 4);
        rows.push(temparray);
    }

    const mappedRows = rows.map((row, idx) => {
        return thumbnails(row, idx, topicId, handleArticleClick)
    });

    return (
        <Grid>
            {mappedRows}
        </Grid>
    );

};

export default TopicPanelView;