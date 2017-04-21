/**
 * Created by kunalwagle on 19/04/2017.
 */

export const ARTICLE_CLICKED = 'ARTICLE_CLICKED';

export function articleClicked(article) {
    return {
        type: ARTICLE_CLICKED,
        article
    }
}