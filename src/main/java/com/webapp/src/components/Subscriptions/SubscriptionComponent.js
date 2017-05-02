/**
 * Created by kunalwagle on 21/04/2017.
 */
import React from "react";
import TopicPanelContainer from "../../containers/TopicViewer/TopicPanelContainer";
import {Tab, NavItem, Row, Col, Nav} from "react-bootstrap";

export const SubscriptionComponent = ({loggedIn, fetchInProgress, topics, handleTopicChange}) => {

    if (!loggedIn) {
        return (
            <div>You must be logged in to access this feature</div>
        )
    }

    if (fetchInProgress) {
        return (
            <div className="loader">Loading...</div>
        )
    }

    if (topics.length === 0) {
        return (
            <div>You have no subscriptions</div>
        )
    }

    const tabMap = topics.map((topic, index) => {
        return (
            <NavItem onSelect={() => handleTopicChange(topic)} eventKey={index} key={index}>
                {topic}
            </NavItem>
        )
    });

    const contentMap = topics.map((topic, index) => {
        return (
            <Tab.Pane eventKey={index} key={index}>
                <TopicPanelContainer topic={topic}/>
            </Tab.Pane>
        )
    });

    return (
        <Tab.Container id="left-tabs-example" defaultActiveKey={0}>
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