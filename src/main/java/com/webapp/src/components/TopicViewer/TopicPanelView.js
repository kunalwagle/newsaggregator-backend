/**
 * Created by kunalwagle on 18/04/2017.
 */
import {map} from "underscore";
import React from "react";
import {Button, Image, Label, Thumbnail} from "react-bootstrap";
import {Link} from "react-router";

let ReactLayoutGrid = require("react-grid-layout");

const TopicPanel = (article, index, handleArticleClick) => {
    return (
        <Button onClick={(event) => {
            handleArticleClick(event, index)
        }}>
            <Thumbnail src={article.articles[0].imageUrl} alt="242x200">
                <h3>{article.articles[0].title}</h3>
            </Thumbnail>
        </Button>
    )
};

const TopicPanelView = ({articles, handleArticleClick}) => {

    let generateLayout = (articles) => {
        return articles.map((article, index) => {
            let x = 0;
            let y = (index / 4) * 10;
            let h = 2;
            let w = 1;
            switch (index % 8) {
                case 0:
                    x = 0;
                    w = 3;
                    break;
                case 1:
                    x = 7;
                    break;
                case 2:
                    x = 9;
                    break;
                case 3:
                    x = 11;
                    break;
                case 4:
                    x = 0;
                    break;
                case 5:
                    x = 2;
                    break;
                case 6:
                    x = 4;
                    break;
                case 7:
                    x = 6;
                    w = 3;
                    break;
                default:
                    break;
            }
            return {
                i: index,
                x,
                y,
                w,
                h,
                static: true,
                maxWidth: 5,
                maxHeight: 5
            }
        });
    };

    let layout = generateLayout(articles);

    const innerReact = (articles) => {
        return articles.map((article, index) => {
            const panel = TopicPanel(article, index, handleArticleClick);
            return (
                <div key={index}>
                    {panel}
                </div>
            )
        });
    };


    return (
        <ReactLayoutGrid layout={layout} cols={12} rowHeight={10} width={1200}>
            {innerReact(articles)}
        </ReactLayoutGrid>
    );

};

export default TopicPanelView;