/**
 * Created by kunalwagle on 06/02/2017.
 */
import React from "react";
import {Jumbotron, Button, Form, FormGroup, FormControl} from "react-bootstrap";
import {Link} from "react-router";
import HomeTopBar from "./HomeTopBar";
import HomePanelContainer from "../../containers/Home/HomePanelContainer";
import SearchBarContainer from "../../containers/Home/SearchBarContainer";

export const SearchBarJumbotron = () => (
    <div>
        <div
            className="row jumbo col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1 col-sm-10 col-sm-offset-1 col-xs-12">
            <img src={"/LogoFullForm.png"} className="jumbotron-image"/>
            <p style={{"textAlign": "center"}}>
                News Aggregator and Summariser
            </p>
            <br/>
            <SearchBarContainer/>
        </div>
        <br/><br/><br/>
        <div className="row">
            <HomeTopBar title="Latest Articles For You"/>
            <br/><br/><br/>
            <HomePanelContainer/>
        </div>
    </div>
);