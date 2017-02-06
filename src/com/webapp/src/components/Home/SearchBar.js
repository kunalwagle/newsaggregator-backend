/**
 * Created by kunalwagle on 06/02/2017.
 */
import React, {Component} from "react";

export class SearchBar extends Component {

    render() {
        return (
            <div>
                <div>
                    <input type="text" value="Insert Search Term Here"/>
                </div>
                <div>
                    <button type="submit">Search</button>
                </div>
            </div>
        )
    }

}