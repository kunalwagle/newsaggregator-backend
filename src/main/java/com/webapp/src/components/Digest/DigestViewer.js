/**
 * Created by kunalwagle on 18/04/2017.
 */
import React from "react";

export const TopicViewerPage = ({params}) => (
    <div>
        <DigestTopBarContainer digestId={params.digestId}/>
        <br/> <br/><br/>
        <DigestPanelContainer digestId={params.digestId}/>
    </div>
);