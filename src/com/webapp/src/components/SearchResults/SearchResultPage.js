/**
 * Created by kunalwagle on 07/02/2017.
 */
import {Form, FormGroup, FormControl, Button} from "react-bootstrap";
import {SearchResults} from "./SearchResult";
import SearchBarContainer from "../../containers/Home/SearchBarContainer";
import React from "react";

export const SearchResultPage = () => (
    <div>
        <div>
            <SearchBarContainer/>
        </div>
        <br/><br/><br/>
        <div>
            <SearchResults/>
        </div>
    </div>
);