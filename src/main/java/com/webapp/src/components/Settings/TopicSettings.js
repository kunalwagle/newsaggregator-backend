/**
 * Created by kunalwagle on 15/05/2017.
 */
import {allOutlets, getPublicationName} from "../../UtilityMethods";
import {contains} from "underscore";
import React from "react";

export const TopicSettings = ({chosenOutlets, digests, topicName, handleOutletChange, handleDigestChange}) => {

    const outlets = allOutlets.map((outlet, index) => {

        let selected = "outletBox col-lg-3 col-md-3 col-sm-3 col-xs-3";

        if (contains(chosenOutlets, outlet)) {
            selected = selected + " selected";
        }

        const imageSource = "/outlets/" + outlet + ".png";

        return (
            <div key={index} className={selected} onClick={() => handleOutletChange(outlet)}>
                <img src={imageSource} className="outletBox-image"/>
                <p>{getPublicationName(outlet)}</p>
            </div>
        )
    });

    return (
        <div className="white">
            <h1>{topicName}</h1>
            <br/>
            <h3>Default sources for summaries</h3>
            {outlets}
            <div className="row">
                <div className="col-lg-8 col-md-8 col-xs-8 col-sm-8">
                    Include in daily digest:
                </div>
                <div className="col-lg-4 col-md-4 col-xs-4 col-sm-4 pull-right">
                    <label className="switch">
                        <input type="checkbox" checked={digests}
                               onChange={event => handleDigestChange(digests)}/>
                        <div className="slider round"></div>
                    </label>
                </div>
            </div>
        </div>
    );

};