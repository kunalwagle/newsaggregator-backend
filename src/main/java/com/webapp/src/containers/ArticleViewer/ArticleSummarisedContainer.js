/**
 * Created by kunalwagle on 20/04/2017.
 */
import {connect} from "react-redux";
import {ArticlesSummarised} from "../../components/ArticleViewer/ArticlesSummarised";
import {annotationsChange, defaultCheckboxes, checkboxChange} from "../../actions/ArticleViewer/ArticleActions";
import {find, uniq, map} from "underscore";

const mapStateToProps = (state) => {

    let fetchInProgressCalled = state.articleViewer.fetchInProgressCalled;
    let sources = state.articleViewer.sources;
    return {
        article: state.articleViewer.article,
        sources: sources,
        fetchInProgressCalled,
        annotations: state.articleViewer.annotations,
        mediaType: state.browser.mediaType
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleAnnotationSwitch: (annotations) => {
            dispatch(annotationsChange(annotations));
        },
        handleDefaultCheckboxes: (article) => {
            dispatch(defaultCheckboxes(article));
        },
        handleCheckboxChange: (event, source) => {
            dispatch(checkboxChange(event.target.checked, source));
        }
    }
};

const ArticleSummarisedContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(ArticlesSummarised);

export default ArticleSummarisedContainer;
