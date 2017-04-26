/**
 * Created by kunalwagle on 07/02/2017.
 */
import {Grid, Row, Col, Thumbnail, Button, Carousel} from "react-bootstrap";
import React from "react";
import {Link} from "react-router";

const thumbnails = (loggedIn, carousel, idx, handleViewClicked, handleSubscribeClicked) => {
    let buttonText = "Subscribe";
    if (!loggedIn) {
        buttonText = "Log in to subscribe";
    }
    return (
        <Carousel.Item key={idx}>
            <Grid>
                <Row>
                    {carousel.map((searchResult, index) => {
                        return (
                            <Col md={3} key={index}>
                                <Thumbnail
                                    src={searchResult.imageUrl}
                                >
                                    <h3>{searchResult.title}</h3>
                                    <p>{searchResult.extract.substring(0, 600) + "..."}</p>
                                    <p>
                                        <Button onClick={(event) => handleViewClicked(event, searchResult.title)}
                                                bsStyle="default">
                                            <Link to="/topic">View</Link>
                                        </Button>
                                        &nbsp;
                                        <Button disabled={!loggedIn} bsStyle="success"
                                                onClick={() => handleSubscribeClicked(searchResult.title)}>{buttonText}</Button>
                                    </p>
                                </Thumbnail>
                            </Col>
                        )
                    })}
                </Row>
            </Grid>
        </Carousel.Item>
    )
};

const carousels = (loggedIn, searchResults, handleViewClicked, handleSubscribeClicked) => {
    if (searchResults.length == 0) {
        return (
            <div>No Search Results...</div>
        )
    }
    let carousels = [];
    for (let i = 0; i < searchResults.length; i += 4) {
        let temparray = searchResults.slice(i, i + 4);
        carousels.push(temparray);
    }

    const mappedCarousels = carousels.map((carousel, idx) => {
        return thumbnails(loggedIn, carousel, idx, handleViewClicked, handleSubscribeClicked)
    });

    return (
        <div>
            <div>
                <h5>Your search returned <b>{searchResults.length}</b> results</h5>
                <br/>
            </div>
            <div><Carousel interval={100000} style={{"height": "750px"}}>{mappedCarousels}</Carousel></div>
        </div>
    )
};

export const SubscriptionComponent = ({loggedIn, searchResults, fetchInProgress, fetchInProgressCalled, searchTerm, handleSearchEmpty, handleViewClicked, handleSubscribeClicked}) => {
    if (fetchInProgress) {
        return (
            <div className="loader">Loading...</div>
        )
    }

    if (!fetchInProgressCalled) {
        handleSearchEmpty(searchTerm);
        return (
            <div className="loader">Loading...</div>
        )
    }

    return carousels(loggedIn, searchResults, handleViewClicked, handleSubscribeClicked);
};