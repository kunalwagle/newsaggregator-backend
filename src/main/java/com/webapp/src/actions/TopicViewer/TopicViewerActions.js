/**
 * Created by kunalwagle on 19/04/2017.
 */
import {push} from "react-router-redux";

export const ARTICLE_CLICKED = 'ARTICLE_CLICKED';

export function articleClicked(topicId, articleId) {
    return (dispatch) => {
        dispatch(push("/topic/" + topicId + "/article/" + articleId));
        return {
            type: ARTICLE_CLICKED
        }
    }
}