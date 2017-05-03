/**
 * Created by kunalwagle on 03/05/2017.
 */

export const ANNOTATIONS_CHANGE = "ANNOTATIONS_CHANGE";

export function annotationsChange(annotations) {
    return {
        type: ANNOTATIONS_CHANGE,
        annotations: !annotations
    }
}


