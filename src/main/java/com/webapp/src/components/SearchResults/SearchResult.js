/**
 * Created by kunalwagle on 07/02/2017.
 */
import {Grid, Row, Col, Thumbnail, Button, Carousel} from "react-bootstrap";
import React from "react";
import {Link} from "react-router";
import {contains, pluck} from "underscore";
import "../../ExtraStyle.css";
import Panel from "../Panel";

const thumbnails = (loggedIn, user, carousel, handleViewClicked, handleSubscribeClicked) => {
    // let buttonText = "Subscribe";
    // if (!loggedIn) {
    //     buttonText = "Log in to subscribe";
    // }
    //
    // const subscribed = (topicId) => {
    //     const ids = pluck(user.topics, "topic");
    //     return contains(ids, topicId);
    // };

    return carousel.map((searchResult, index) => {
        let image = "thumbnail";
        if (searchResult.imageUrl != undefined) {
            image = searchResult.imageUrl;
        }
        return (
            <div key={index} className="col-lg-3 col-md-3 col-sm-6 col-xs-12">
                <Panel key={index} image={image} title={searchResult.title}
                       largePanel={true}
                       text={searchResult.extract.substring(0, 200) + "..."}/>

            </div>

        )
    })
}

const carousels = (loggedIn, user, searchResults, handleViewClicked, handleSubscribeClicked) => {
    if (searchResults.length == 0) {
        return (
            <div>No Search Results...</div>
        )
    }
    // let carousels = [];
    // for (let i = 0; i < searchResults.length; i += 4) {
    //     let temparray = searchResults.slice(i, i + 4);
    //     carousels.push(temparray);
    // }



    return (
        <div>
            <div>
                <h5>Your search returned <b>{searchResults.length}</b> results</h5>
                <br/>
            </div>
            <div>
                <div className="container">
                    <div className="row">
                        {thumbnails(loggedIn, user, searchResults, handleViewClicked, handleSubscribeClicked)}
                    </div>
                </div>
            </div>
        </div>
    )
};

export const SubscriptionComponent = ({loggedIn, user, searchResults, fetchInProgress, fetchInProgressCalled, searchTerm, handleSearchEmpty, handleViewClicked, handleSubscribeClicked}) => {
    if (fetchInProgress) {
        return (
            <div className="loader"></div>
        )
    }

    if (!fetchInProgressCalled) {
        handleSearchEmpty(searchTerm);
        return (
            <div className="loader"></div>
        )
    }

    return carousels(loggedIn, user, searchResults, handleViewClicked, handleSubscribeClicked);
};