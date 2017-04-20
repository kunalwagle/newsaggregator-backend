/**
 * Created by kunalwagle on 19/04/2017.
 */

export const ARTICLE_CLICKED = 'ARTICLE_CLICKED';
export const SUBSCRIBE_CLICKED = 'SUBSCRIBE_CLICKED';

export function articleClicked(article) {
    return {
        type: ARTICLE_CLICKED,
        article
    }
}

export function subscribeClicked(label) {
    return {
        type: SUBSCRIBE_CLICKED,
        label
    }
}