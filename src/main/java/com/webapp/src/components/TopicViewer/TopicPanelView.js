/**
 * Created by kunalwagle on 18/04/2017.
 */
import {map} from "underscore";
import React from "react";
import * as ReactLayoutGrid from "react-layout-grid";
import {Button, Image, Label} from "react-bootstrap";

const TopicPanel = (article, index, handleArticleClick) => {
    return (
        <Button onClick={(event) => {
            handleArticleClick(event, index)
        }}>
            <Image src={article.imageUrl} rounded>
                <Label>{article.title}</Label>
            </Image>
        </Button>
    )
};

export const TopicPanelView = ({articles, handleArticleClick}) => {

    let generateLayout = (articles) => {
        return map(articles, (article, index) => {
            let x = 0;
            let y = (index / 4) * 10;
            let h = 8;
            let w = 2;
            switch (index % 8) {
                case 0:
                    x = 0;
                    w = 6;
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
                    w = 6;
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
                static: true
            }
        });
    };

    let layout = generateLayout(articles);

    return (
        <ReactLayoutGrid layout={layout} cols={12} rowHeight={30} width={1200}>
            {
                map(articles, (article, index) => {
                    return (
                        <div key={index}>
                            {TopicPanel(article, index, handleArticleClick)}
                        </div>
                    )
                })
            }
        </ReactLayoutGrid>
    )

};