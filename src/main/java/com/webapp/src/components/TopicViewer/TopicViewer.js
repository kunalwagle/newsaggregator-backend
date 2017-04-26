/**
 * Created by kunalwagle on 18/04/2017.
 */
import React from "react";
import TopicPanelContainer from "../../containers/TopicViewer/TopicPanelContainer";
import TopicTopBarContainer from "../../containers/TopicViewer/TopicTopBarContainer";

export const TopicViewerPage = ({params}) => (
    <div>
        <TopicTopBarContainer/>
        <br/> <br/><br/>
        <TopicPanelContainer topicId={params.topicId}/>
    </div>
);