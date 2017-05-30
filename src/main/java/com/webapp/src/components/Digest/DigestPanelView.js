/**
 * Created by kunalwagle on 18/04/2017.
 */
import {map, filter, size} from "underscore";
import React from "react";
import {Button, Image, Label, Thumbnail, Grid, Row, Col} from "react-bootstrap";
import {Link} from "react-router";
import ExtraLargePanel from "../TopicViewer/ExtraLargePanel";
import Panel from "../Panel";
import ListPanel from "../TopicViewer/ListPanel";

function findBootstrapEnvironment() {
    let envs = ["xs", "sm", "md", "lg"],
        doc = window.document,
        temp = doc.createElement("div");

    doc.body.appendChild(temp);

    for (let i = envs.length - 1; i >= 0; i--) {
        const env = envs[i];

        temp.className = "hidden-" + env;

        if (temp.offsetParent === null) {
            doc.body.removeChild(temp);
            return env;
        }
    }
    return "";
}

const thumbnails = (row, idx, topicId, handleArticleClick) => {
    return (
        <Row key={idx}>
            {row.map((article, index) => {
                return (
                    <Col md={3} key={index}>
                        <Thumbnail src={article.imageUrl}>
                            <h4>{article.title}</h4>
                            <Button
                                onClick={(event) => handleArticleClick(event, article.topicId, article.articleId)}
                                bsStyle="default">
                                View
                            </Button>
                        </Thumbnail>
                    </Col>
                )
            })}
        </Row>
    )
};

const md = (articles, handleArticleClick) => {
    const largePanels = articles.slice(0, 1);
    const smallPanels = articles.slice(1);

    return (
        <div>
            <div>
                {largePanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.imageUrl != undefined) {
                        imageUrl = article.imageUrl;
                    }
                    return (
                        <div className="col-md-12" key={index}>
                            <ExtraLargePanel key={index} title={article.title} image={imageUrl}
                                             onClick={(event) => handleArticleClick(event, article.topicId, article.articleId)}/>
                        </div>
                    )
                })}
            </div>
            <br/>
            <div>
                {smallPanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.imageUrl != undefined) {
                        imageUrl = article.imageUrl;
                    }
                    return (
                        <div className="col-md-6 col-sm-6 col-lg-6" key={index}>
                            <Panel image={imageUrl} key={index} title={article.title}
                                   onClick={(event) => handleArticleClick(event, article.topicId, article.articleId)}/>
                        </div>
                    )
                })}
            </div>
        </div>
    )
};

const lg = (articles, handleArticleClick) => {
    const largePanels = articles.slice(0, 2);
    const smallPanels = articles.slice(2);

    return (
        <div>
            <div className="row">
                {largePanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.imageUrl != undefined) {
                        imageUrl = article.imageUrl;
                    }
                    return (

                        <div className="col-lg-6" key={index}>
                            <ExtraLargePanel key={index} title={article.title} image={imageUrl}
                                             onClick={(event) => handleArticleClick(event, article.topicId, article.articleId)}/>
                        </div>
                    )
                })}
            </div>
            <br/>
            <div className="row">
                {smallPanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.imageUrl != undefined) {
                        imageUrl = article.imageUrl;
                    }
                    return (
                        <div className="col-lg-3 col-md-6" key={index}>
                            <Panel image={imageUrl} key={index} title={article.title}
                                   onClick={(event) => handleArticleClick(event, article.topicId, article.articleId)}/>
                        </div>
                    )
                })}
            </div>
        </div>
    )
};

const sm = (articles, handleArticleClick) => {
    const largePanels = articles.slice(0, 1);
    const smallPanels = articles.slice(1);

    return (
        <div>
            <div>
                {largePanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.imageUrl != undefined) {
                        imageUrl = article.imageUrl;
                    }
                    return (
                        <div className="col-sm-12" key={index}>
                            <ExtraLargePanel key={index} title={article.title} image={imageUrl}
                                             onClick={(event) => handleArticleClick(event, article.topicId, article.articleId)}/>
                        </div>
                    )
                })}
            </div>
            <br/>
            <div>
                {smallPanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.imageUrl != undefined) {
                        imageUrl = article.imageUrl;
                    }
                    return (
                        <div className="col-sm-6" key={index}>
                            <Panel image={imageUrl} key={index} title={article.title}
                                   onClick={(event) => handleArticleClick(event, article.topicId, article.articleId)}/>
                        </div>
                    )
                })}
            </div>
        </div>
    )
};

const xs = (articles, handleArticleClick) => {
    const largePanels = articles.slice(0, 2);
    const smallPanels = articles.slice(2);

    return (
        <div>
            <div className="row">
                {largePanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.imageUrl != undefined) {
                        imageUrl = article.imageUrl;
                    }
                    return (
                        <div className="col-xs-12" key={index}>
                            <Panel key={index} title={article.title} image={imageUrl}
                                   onClick={(event) => handleArticleClick(event, article.topicId, article.articleId)}/>
                        </div>
                    )
                })}
            </div>
            <br/>
            <div className="row">
                {smallPanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.imageUrl != undefined) {
                        imageUrl = article.imageUrl;
                    }
                    return (
                        <div className="col-xs-12" key={index}>
                            <ListPanel image={imageUrl} key={index} title={article.title}
                                       onClick={(event) => handleArticleClick(event, article.topicId, article.articleId)}/>
                        </div>
                    )
                })}
            </div>
        </div>
    )
};


const DigestPanelView = ({articles, mediaType, fetchInProgress, fetchInProgressCalled, digestId, handleReloadNeeded, handleArticleClick}) => {

    if (fetchInProgress) {
        return (
            <div className="loader"></div>
        )
    }

    if (!fetchInProgressCalled) {
        handleReloadNeeded(digestId);
        return (
            <div className="loader"></div>
        )
    }

    if (articles.length === 0) {
        return (
            <div className="nothing">There is no recent news in your digest</div>
        )
    }

    switch (mediaType) {
        case "extraSmall":
        case "small":
            return xs(articles, handleArticleClick);
        case "medium":
            return sm(articles, handleArticleClick);
        case "large":
            return md(articles, handleArticleClick);
        default:
            return lg(articles, handleArticleClick);
    }

    // let rows = [];
    // for (let i = 0; i < articles.length; i += 4) {
    //     let temparray = articles.slice(i, i + 4);
    //     rows.push(temparray);
    // }
    //
    // const mappedRows = rows.map((row, idx) => {
    //     return thumbnails(row, idx, topicId, handleArticleClick)
    // });
    //
    // return (
    //     <Grid>
    //         {mappedRows}
    //     </Grid>
    // );

};

export default DigestPanelView;