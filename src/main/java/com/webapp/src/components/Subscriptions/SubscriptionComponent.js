/**
 * Created by kunalwagle on 21/04/2017.
 */
import React from "react";
import TopicPanelContainer from "../../containers/TopicViewer/TopicPanelContainer";
import {Tab, NavItem, Row, Col, Nav} from "react-bootstrap";

export const SubscriptionComponent = ({fetchInProgress, topics, handleTopicChange}) => {

    if (fetchInProgress) {
        return (
            <div className="loader">Loading...</div>
        )
    }

    const tabMap = topics.map((topic, index) => {
        return (
            <NavItem onSelect={() => handleTopicChange(topic.clusterHolders)} eventKey={index} key={index}>
                {topic.topic}
            </NavItem>
        )
    });

    const contentMap = topics.map((topic, index) => {
        return (
            <Tab.Pane eventKey={index} key={index}>
                <TopicPanelContainer/>
            </Tab.Pane>
        )
    });

    return (
        <Tab.Container defaultActiveKey={0}>
            <Row className="clearfix">
                <Col sm={4}>
                    <Nav bsStyle="pills" stacked>
                        {tabMap}
                    </Nav>
                </Col>
                <Col sm={8}>
                    <Tab.Content animation>
                        {contentMap}
                    </Tab.Content>
                </Col>
            </Row>
        </Tab.Container>
    )

};