/**
 * Created by kunalwagle on 04/04/2017.
 */
import {
    FIRST_CHANGED,
    SECOND_CHANGED,
    THIRD_CHANGED,
    SUMMARY_RESULTS_RECEIVED,
    SUMMARY_START
} from "../../actions/SummaryEvaluation/SummaryEvaluationActions";

const initialState = {
    firstBoxText: "",
    secondBoxText: "",
    thirdBoxText: "",
    fetchInProgress: false
};

export default function summaryEvaluation(state, action) {
    if (typeof state === 'undefined') {
        return initialState;
    }

    switch (action.type) {
        case FIRST_CHANGED:
            return Object.assign({}, state, {
                firstBoxText: action.newValue
            });
        case SECOND_CHANGED:
            return Object.assign({}, state, {
                secondBoxText: action.newValue
            });
        case THIRD_CHANGED:
            return Object.assign({}, state, {
                thirdBoxText: action.newValue
            });
        case SUMMARY_START:
            return Object.assign({}, state, {
                fetchInProgress: true,
            });
        case SUMMARY_RESULTS_RECEIVED:
            return Object.assign({}, state, {
                fetchInProgress: false,
                extractive: action.json.extractive,
                abstractive: action.json.abstractive
            })
    }

    return state;
}
