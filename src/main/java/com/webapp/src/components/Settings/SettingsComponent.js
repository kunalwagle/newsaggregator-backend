/**
 * Created by kunalwagle on 21/04/2017.
 */
import React from "react";
import {Tab, NavItem, Row, Col, Nav} from "react-bootstrap";
import TopicSettingsContainer from "../../containers/Settings/TopicSettingsContainer";

export const SettingsComponent = ({loggedIn, fetchInProgress, fetchInProgressLoginCalled, fetchInProgressCalled, user, index, handleLogout, handleReloadLogin, handleTopicChange, handleReloadNeeded}) => {

    if (!loggedIn) {
        return (
            <div className="nothing">You must be logged in to access this feature</div>
        )
    }

    if (!fetchInProgressLoginCalled || user == undefined) {
        handleReloadLogin();
        return (
            <div className="loader"></div>
        )
    }

    if (!fetchInProgressCalled) {
        handleReloadNeeded(user.topics[0]);
        return (
            <div className="loader"></div>
        )
    }

    if (fetchInProgress) {
        return (
            <div className="loader"></div>
        )
    }

    if (user.topics.length === 0) {
        return (
            <div>
                <div className="nothing">You have no subscriptions</div>
                <br/><br/>
                <button
                    className="danger-button col-lg-2 col-md-2 col-xs-6 col-sm-12 col-lg-offset-5 col-md-offset-5 col-xs-offset-3"
                    onClick={handleLogout}>Log Out
                </button>
            </div>
        )
    }

    const tabMap = user.topics.map((topic, index) => {
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
                    <br/><br/>
                    <button className="danger-button col-lg-10 col-md-10 col-xs-10 col-sm-10 margined"
                            onClick={handleLogout}>Log Out
                    </button>
                </Col>
                <Col sm={8}>
                    <TopicSettingsContainer />
                </Col>
            </Row>
        </Tab.Container>
    )

};
