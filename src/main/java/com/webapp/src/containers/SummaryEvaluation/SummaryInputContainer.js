/**
 * Created by kunalwagle on 04/04/2017.
 */
import {connect} from "react-redux";
import {SummaryInput} from "../../components/SummaryEvaluation/SummaryInput";
import {
    secondChanged,
    firstChanged,
    thirdChanged,
    summarise
} from "../../actions/SummaryEvaluation/SummaryEvaluationActions";

const mapStateToProps = (state) => {
    return {
        firstBoxText: state.summaryEvaluation.firstBoxText,
        secondBoxText: state.summaryEvaluation.secondBoxText,
        thirdBoxText: state.summaryEvaluation.thirdBoxText,
        extractive: state.summaryEvaluation.extractive,
        abstractive: state.summaryEvaluation.abstractive
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleFirstChange: (event) => {
            dispatch(firstChanged(event.target.value))
        },
        handleSecondChange: (event) => {
            dispatch(secondChanged(event.target.value))
        },
        handleThirdChange: (event) => {
            dispatch(thirdChanged(event.target.value))
        },
        handleSubmit: () => {
            dispatch(summarise())
        }
    }
};

const SummaryInputContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SummaryInput);

export default SummaryInputContainer;