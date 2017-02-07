/**
 * Created by kunalwagle on 07/02/2017.
 */
import {Grid, Row, Col, Thumbnail, Button} from "react-bootstrap";
import React from "react";

export const SearchResults = ({searchResults, fetchInProgress}) => {
    fetchInProgress = true;
    if (fetchInProgress) {
        return (
            <div className="loader"></div>
        )
    }
    return (

        <Grid>
            <Row>
                <Col xs={6} md={4}>
                    <Thumbnail src="/assets/thumbnaildiv.png" alt="242x200">
                        <h3>Thumbnail label</h3>
                        <p>Description</p>
                        <p>
                            <Button bsStyle="primary">Button</Button>&nbsp;
                            <Button bsStyle="default">Button</Button>
                        </p>
                    </Thumbnail>
                </Col>
                <Col xs={6} md={4}>
                    <Thumbnail src="/assets/thumbnaildiv.png" alt="242x200">
                        <h3>Thumbnail label</h3>
                        <p>Description</p>
                        <p>
                            <Button bsStyle="primary">Button</Button>&nbsp;
                            <Button bsStyle="default">Button</Button>
                        </p>
                    </Thumbnail>
                </Col>
                <Col xs={6} md={4}>
                    <Thumbnail src="/assets/thumbnaildiv.png" alt="242x200">
                        <h3>Thumbnail label</h3>
                        <p>Description</p>
                        <p>
                            <Button bsStyle="primary">Button</Button>&nbsp;
                            <Button bsStyle="default">Button</Button>
                        </p>
                    </Thumbnail>
                </Col>
            </Row>
        </Grid>

    );
}