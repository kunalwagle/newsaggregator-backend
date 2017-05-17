/**
 * Created by kunalwagle on 18/04/2017.
 */
import {map, filter, size} from "underscore";
import React from "react";
import {Button, Image, Label, Thumbnail, Grid, Row, Col} from "react-bootstrap";
import {Link} from "react-router";
import ExtraLargePanel from "./ExtraLargePanel";
import Panel from "../Panel";
import ListPanel from "./ListPanel";

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
                        <Thumbnail src={article.articles[0].imageUrl}>
                            <h4>{article.articles[0].title}</h4>
                            <Button onClick={(event) => handleArticleClick(event, topicId, article.id)}
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

const md = (articles, handleArticleClick, topicId) => {
    const largePanels = articles.slice(0, 1);
    const smallPanels = articles.slice(1);

    return (
        <div>
            <div>
                {largePanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.articles[0].imageUrl != undefined) {
                        imageUrl = article.articles[0].imageUrl;
                    }
                    return (
                        <div className="col-md-12" key={index}>
                            <ExtraLargePanel key={index} title={article.articles[0].title} image={imageUrl}
                                             onClick={(event) => handleArticleClick(event, topicId, article.id)}/>
                        </div>
                    )
                })}
            </div>
            <br/>
            <div>
                {smallPanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.articles[0].imageUrl != undefined) {
                        imageUrl = article.articles[0].imageUrl;
                    }
                    return (
                        <div className="col-md-6 col-sm-6 col-lg-6" key={index}>
                            <Panel image={imageUrl} key={index} title={article.articles[0].title}
                                   onClick={(event) => handleArticleClick(event, topicId, article.id)}/>
                        </div>
                    )
                })}
            </div>
        </div>
    )
};

const lg = (articles, handleArticleClick, topicId) => {
    const largePanels = articles.slice(0, 2);
    const smallPanels = articles.slice(2);

    return (
        <div>
            <div className="row">
                {largePanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.articles[0].imageUrl != undefined) {
                        imageUrl = article.articles[0].imageUrl;
                    }
                    return (

                        <div className="col-lg-6" key={index}>
                            <ExtraLargePanel key={index} title={article.articles[0].title} image={imageUrl}
                                             onClick={(event) => handleArticleClick(event, topicId, article.id)}/>
                        </div>
                    )
                })}
            </div>
            <br/>
            <div className="row">
                {smallPanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.articles[0].imageUrl != undefined) {
                        imageUrl = article.articles[0].imageUrl;
                    }
                    return (
                        <div className="col-lg-3 col-md-6" key={index}>
                            <Panel image={imageUrl} key={index} title={article.articles[0].title}
                                   onClick={(event) => handleArticleClick(event, topicId, article.id)}/>
                        </div>
                    )
                })}
            </div>
        </div>
    )
};

const sm = (articles, handleArticleClick, topicId) => {
    const largePanels = articles.slice(0, 1);
    const smallPanels = articles.slice(1);

    return (
        <div>
            <div>
                {largePanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.articles[0].imageUrl != undefined) {
                        imageUrl = article.articles[0].imageUrl;
                    }
                    return (
                        <div className="col-sm-12" key={index}>
                            <ExtraLargePanel key={index} title={article.articles[0].title} image={imageUrl}
                                             onClick={(event) => handleArticleClick(event, topicId, article.id)}/>
                        </div>
                    )
                })}
            </div>
            <br/>
            <div>
                {smallPanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.articles[0].imageUrl != undefined) {
                        imageUrl = article.articles[0].imageUrl;
                    }
                    return (
                        <div className="col-sm-6" key={index}>
                            <Panel image={imageUrl} key={index} title={article.articles[0].title}
                                   onClick={(event) => handleArticleClick(event, topicId, article.id)}/>
                        </div>
                    )
                })}
            </div>
        </div>
    )
};

const xs = (articles, handleArticleClick, topicId) => {
    const largePanels = articles.slice(0, 2);
    const smallPanels = articles.slice(2);

    return (
        <div>
            <div className="row">
                {largePanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.articles[0].imageUrl != undefined) {
                        imageUrl = article.articles[0].imageUrl;
                    }
                    return (
                        <div className="col-xs-12" key={index}>
                            <Panel key={index} title={article.articles[0].title} image={imageUrl}
                                   onClick={(event) => handleArticleClick(event, topicId, article.id)}/>
                        </div>
                    )
                })}
            </div>
            <br/>
            <div className="row">
                {smallPanels.map((article, index) => {
                    let imageUrl = "thumbnail";
                    if (article.articles[0].imageUrl != undefined) {
                        imageUrl = article.articles[0].imageUrl;
                    }
                    return (
                        <div className="col-xs-12" key={index}>
                            <ListPanel image={imageUrl} key={index} title={article.articles[0].title}
                                       onClick={(event) => handleArticleClick(event, topicId, article.id)}/>
                        </div>
                    )
                })}
            </div>
        </div>
    )
};


const TopicPanelView = ({articles, mediaType, fetchInProgress, fetchInProgressCalled, topicId, topic, handleReloadNeeded, handleArticleClick}) => {

    if (fetchInProgress) {
        return (
            <div className="loader"></div>
        )
    }

    if (!fetchInProgressCalled) {
        handleReloadNeeded(topicId, topic);
        return (
            <div className="loader"></div>
        )
    }


    articles = filter(articles, function (article) {
        return article.articles.length && article.articles[0] != null;
    });

    if (articles.length === 0) {
        return (
            <div>There is no recent news for this topic</div>
        )
    }

    switch (mediaType) {
        case "extraSmall":
        case "small":
            return xs(articles, handleArticleClick, topicId);
        case "medium":
            return sm(articles, handleArticleClick, topicId);
        case "large":
            return md(articles, handleArticleClick, topicId);
        default:
            return lg(articles, handleArticleClick, topicId);
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

export default TopicPanelView;