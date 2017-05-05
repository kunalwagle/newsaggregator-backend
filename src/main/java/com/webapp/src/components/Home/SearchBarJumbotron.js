/**
 * Created by kunalwagle on 06/02/2017.
 */
import React from "react";
import {Jumbotron, Button, Form, FormGroup, FormControl} from "react-bootstrap";
import {Link} from "react-router";
import SearchBarContainer from "../../containers/Home/SearchBarContainer";

export const SearchBarJumbotron = () => (
    <div>
        <Jumbotron className="jumbotron">
            <h2 style={{"textAlign": "center"}}>
                News Aggregator and Summariser
            </h2>
            <br></br>
            <p style={{"textAlign": "center"}}>
                I really need to think of a better name...
            </p>
            <br></br>
            <SearchBarContainer/>
        </Jumbotron>
    </div>
);