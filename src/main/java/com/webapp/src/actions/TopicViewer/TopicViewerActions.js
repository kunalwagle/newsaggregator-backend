/**
 * Created by kunalwagle on 19/04/2017.
 */
import {push} from "react-router-redux";
import {find} from "underscore";
import {fetchInProgressToggle} from "../ArticleViewer/ArticleActions";

export const ARTICLE_CLICKED = 'ARTICLE_CLICKED';
export const PAGE_CHANGE = 'PAGE_CHANGE';

export function articleClicked(topicId, articleId) {
    return (dispatch) => {
        dispatch(push("/topic/" + topicId + "/article/" + articleId));
        dispatch(fetchInProgressToggle());
        return {
            type: ARTICLE_CLICKED
        }
    }
}



