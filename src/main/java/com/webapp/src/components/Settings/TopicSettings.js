/**
 * Created by kunalwagle on 15/05/2017.
 */
import {allOutlets, getPublicationName} from "../../UtilityMethods";
import {contains} from "underscore";
import React from "react";

export const TopicSettings = ({chosenOutlets, digest, topic, handleOutletChange, handleDigestChange, handleSave, handleUnsubscribe}) => {

    if (topic == undefined) {
        return (<div></div>)
    }

    const outlets = allOutlets.map((outlet, index) => {

        let selected = "outletBox col-lg-2 col-md-2 col-sm-2 col-xs-2";

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
            <h1>{topic.labelName}</h1>
            <br/>
            <h3>Default sources for summaries</h3>
            <div className="row">
                {outlets}
            </div>
            <br/><br/>
            <div className="row">
                <div className="col-lg-8 col-md-8 col-xs-8 col-sm-8">
                    Include in daily digest:
                </div>
                <div className="col-lg-4 col-md-4 col-xs-4 col-sm-4 pull-right">
                    <label className="switch">
                        <input type="checkbox" checked={digest}
                               onChange={event => handleDigestChange(digest)}/>
                        <div className="slider round"></div>
                    </label>
                </div>
            </div>
            <br/><br/>
            <div className="row">
                <button className="danger-button col-lg-3 col-md-3 col-xs-3 col-sm-3"
                        onClick={() => handleUnsubscribe(topic.topicId)}>Unsubscribe
                </button>
                <button className="standard-button col-lg-3 col-md-3 col-xs-3 col-sm-3"
                        onClick={() => handleSave()}>Apply to All
                </button>
                <button className="standard-button col-lg-3 col-md-3 col-xs-3 col-sm-3"
                        onClick={() => handleSave(topic.topicId)}>
                    Save
                </button>
            </div>

        </div>
    );

};