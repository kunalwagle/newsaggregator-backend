/**
 * Created by kunalwagle on 21/04/2017.
 */
import React from "react";
import {Tab, NavItem, Row, Col, Nav, OverlayTrigger} from "react-bootstrap";
import LoginModal from "../LoginModal";
import TopicSettingsContainer from "../../containers/Settings/TopicSettingsContainer";

export const SettingsComponent = ({loggedIn, fetchInProgress, fetchInProgressLoginCalled, fetchInProgressCalled, topics, index, handleLogout, handleReloadLogin, handleTopicChange, handleLoginClicked, handleSettingsSearch, handleReloadNeeded}) => {

    const loginPopover = (handleLoginClicked, action) => {
        return (
            <LoginModal handleLoginClicked={handleLoginClicked} action={action}/>
        );
    };

    if (!loggedIn) {
        return (
            <div>
                <div className="nothing">You must be logged in to access this feature.
                    <br/><br/>
                    <h3>How about registering now? It's simple</h3>
                    <br/><br/>
                    <OverlayTrigger trigger="click" rootClose placement="bottom" container={this}
                                    overlay={loginPopover(handleLoginClicked, handleSettingsSearch)}>
                        <button className="search-bar-button">Register or Login</button>
                    </OverlayTrigger>
                </div>
            </div>

        )
    }

    if (!fetchInProgressLoginCalled || topics == undefined) {
        handleReloadLogin();
        return (
            <div className="loader"></div>
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

    const tabMap = topics.map((topic, index) => {
        return (
            <NavItem onSelect={() => handleTopicChange(topic, index)} eventKey={index} key={index}>
                <div className="white">{topic.labelName}</div>
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
