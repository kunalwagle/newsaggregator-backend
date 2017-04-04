/**
 * Created by kunalwagle on 04/04/2017.
 */
import {connect} from "react-redux";
import {SummaryInput} from "../../components/SummaryEvaluation/SummaryInput";

const mapStateToProps = (state) => {
    return {
        firstBoxText: state.summaryEvaluation.firstBoxText,
        secondBoxText: state.summaryEvaluation.secondBoxText,
        thirdBoxText: state.summaryEvaluation.thirdBoxText,
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleFirstChange: (event) => {
            dispatch(firstBoxChange(event.target.value))
        },
        handleSecondChange: (event) => {
            dispatch(secondBoxChange(event.target.value))
        },
        handleThirdChange: (event) => {
            dispatch(thirdBoxChange(event.target.value))
        },
        handleSubmit: () => {
            dispatch(submit())
        }
    }
};

const SummaryInputContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SummaryInput);

export default SummaryInputContainer;