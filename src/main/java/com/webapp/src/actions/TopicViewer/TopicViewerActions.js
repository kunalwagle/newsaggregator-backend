/**
 * Created by kunalwagle on 19/04/2017.
 */
import {push} from "react-router-redux";
import {find} from "underscore";
import {defaultCheckboxes} from "../ArticleViewer/ArticleActions";

export const ARTICLE_CLICKED = 'ARTICLE_CLICKED';

export function articleClicked(topicId, articleId) {
    return (dispatch, getState) => {
        let article = find(getState().searchResults.articles, (art) => {
            return art.id === articleId;
        });
        dispatch(defaultCheckboxes(article));
        dispatch(push("/topic/" + topicId + "/article/" + articleId));
        return {
            type: ARTICLE_CLICKED
        }
    }
}