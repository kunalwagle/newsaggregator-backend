/**
 * Created by kunalwagle on 07/02/2017.
 */
import {Form, FormGroup, FormControl, Button} from "react-bootstrap";
import SearchResultsContainer from "../../containers/SearchResults/SearchResultsContainer";
import SearchBarContainer from "../../containers/Home/SearchBarContainer";
import React from "react";

export const SearchResultPage = ({params}) => (
    <div>
        <div>
            <SearchBarContainer/>
        </div>
        <br/><br/><br/>
        <div>
            <SearchResultsContainer searchTerm={params.searchTerm}/>
        </div>
    </div>
);