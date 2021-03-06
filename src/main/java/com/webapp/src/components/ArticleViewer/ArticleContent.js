/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import {PageHeader, Image, OverlayTrigger} from "react-bootstrap";
import {pluck, isEmpty, isEqual, uniq} from "underscore";
import {getColour} from "../../UtilityMethods";
import SentenceSourcesComponent from "./SentenceSourcesComponent";
import moment from "moment";

export const ArticleContent = ({article, sources, articleId, topicId, annotations, fetchInProgressCalled, handleReloadNeeded}) => {

    if (!fetchInProgressCalled) {
        handleReloadNeeded(articleId, topicId);
        return (
            <div className="loader"></div>
        )
    }

    const getSummary = () => {
        sources = sources.sort();
        let uniqsources = uniq(sources, true);
        let summaryMap = [];
        for (let key in article.summaryMap) {
            if (!article.summaryMap.hasOwnProperty(key)) continue;
            if ("[" + sources.toString() + "]" === key) {
                summaryMap = article.summaryMap[key];
            } else if ("[" + uniqsources.toString() + "]" === key) {
                summaryMap = article.summaryMap[key];
            }
        }
        return summaryMap;
    };

    const stripIntoSentences = (summary) => pluck(summary, "sentence");

    const paragraphsPlain = (node) => {
        return node.map((text, index) => <p key={index}>{text}</p>)
            .reduce((nodes, node) => nodes.concat(node), []);
    };

    const sentencePopover = (text) => {

        let sentences = [text];
        sentences = sentences.concat(text.relatedNodes);

        return (
            <SentenceSourcesComponent sentences={sentences}/>
        );
    };

    const paragraphsAnnotated = (node) => {
        return node.map((text, index) => {
            if (text.relatedNodes == undefined) {
                return (
                    <OverlayTrigger key={index} container={this} trigger="click" rootClose placement="top"
                                    overlay={sentencePopover(text)}>
                        <p key={index} style={{"backgroundColor": getColour(text.source)}}>
                            {text.sentence}
                        </p>
                    </OverlayTrigger>
                )
            } else if (text.relatedNodes.length === 0) {
                return (
                    <OverlayTrigger key={index} container={this} trigger="click" rootClose placement="top"
                                    overlay={sentencePopover(text)}>
                        <p key={index} style={{"backgroundColor": getColour(text.source)}}>
                            {text.sentence}
                        </p>
                    </OverlayTrigger>
                )
            } else {
                return (
                    <OverlayTrigger key={index} container={this} trigger="click" rootClose placement="top"
                                    overlay={sentencePopover(text)}>
                        <p key={index}>{text.sentence}</p>
                    </OverlayTrigger>
                )
            }
        })
            .reduce((nodes, node) => nodes.concat(node), []);
    };

    const paragraph = () => {
        if (!annotations) {
            return (<div className="image-width">{paragraphsPlain(stripIntoSentences(getSummary()))}</div>)
        } else {
            return (<div className="image-width">{paragraphsAnnotated(getSummary())}</div>)
        }
    };

    return (
        <div className="white">
            <h1>
                {article.articles[0].title}
            </h1>
            <h4>{moment(new Date(article.lastPublished)).format("LLLL")}</h4>
            <img src={article.articles[0].imageUrl} className="image-width"/>
            <br/><br/>
            {paragraph()}
        </div>
    )

};