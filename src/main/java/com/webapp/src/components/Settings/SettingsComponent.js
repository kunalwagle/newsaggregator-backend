/**
 * Created by kunalwagle on 21/04/2017.
 */
import React from "react";
import {Tab, NavItem, Row, Col, Nav} from "react-bootstrap";
import TopicSettingsContainer from "../../containers/Settings/TopicSettingsContainer";

export const SettingsComponent = ({loggedIn, fetchInProgress, fetchInProgressCalled, topics, index, handleTopicChange, handleReloadNeeded}) => {

    if (!loggedIn) {
        return (
            <div>You must be logged in to access this feature</div>
        )
    }

    if (!fetchInProgressCalled) {
        handleReloadNeeded(topics[0]);
        return (
            <div className="loader"></div>
        )
    }

    if (fetchInProgress) {
        return (
            <div className="loader"></div>
        )
    }

    if (topics.length === 0) {
        return (
            <div>You have no subscriptions</div>
        )
    }

    const tabMap = topics.map((topic, index) => {
        return (
            <NavItem onSelect={() => handleTopicChange(topic, index)} eventKey={index} key={index}>
                <div className="white">{topic.labelHolder.label}</div>
            </NavItem>
        )
    });

    return (
        <Tab.Container id="left-tabs-example" defaultActiveKey={index}>
            <Row className="clearfix">
                <Col sm={3}>
                    <Nav bsStyle="pills" stacked>
                        {tabMap}
                    </Nav>
                </Col>
                <Col sm={8}>
                    <TopicSettingsContainer />
                </Col>
            </Row>
        </Tab.Container>
    )

};
