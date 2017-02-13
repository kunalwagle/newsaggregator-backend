/**
 * Created by kunalwagle on 06/02/2017.
 */
import React from "react";
import {Jumbotron, Button, Form, FormGroup, FormControl} from "react-bootstrap";
import {Link} from "react-router";
import SearchBarContainer from "../../containers/Home/SearchBarContainer";

export const SearchBarJumbotron = () => (
    <Jumbotron className="col-md-8 col-md-offset-2">
        <h2 style={{"textAlign": "center"}}>
            News Aggregator and Summariser
        </h2>
        <br></br>
        <p style={{"textAlign": "center"}}>
            I really need to think of a better name...
        </p>
        <br></br>
        <p className="col-md-6 col-md-offset-3">
            <SearchBarContainer/>
        </p>
    </Jumbotron>
);