/**
 * Created by kunalwagle on 03/05/2017.
 */
import {map, uniq} from "underscore";

export const ANNOTATIONS_CHANGE = "ANNOTATIONS_CHANGE";
export const CHECKBOX_CHANGE = "CHECKBOX_CHANGE";
export const DEFAULT_CHECKBOXES = "DEFAULT_CHECKBOXES";

export function annotationsChange(annotations) {
    return {
        type: ANNOTATIONS_CHANGE,
        annotations: !annotations
    }
}

export function checkboxChange(on, source) {
    return {
        type: CHECKBOX_CHANGE,
        source,
        on: on
    }
}

export function defaultCheckboxes(article) {
    let sources = map(article.articles, (art) => {
        return art.source;
    });
    sources = uniq(sources);
    return {
        type: DEFAULT_CHECKBOXES,
        sources
    }
}


