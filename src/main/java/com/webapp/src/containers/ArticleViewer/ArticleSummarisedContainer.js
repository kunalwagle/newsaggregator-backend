/**
 * Created by kunalwagle on 20/04/2017.
 */
import {connect} from "react-redux";
import {ArticlesSummarised} from "../../components/ArticleViewer/ArticlesSummarised";
import {annotationsChange, defaultCheckboxes, checkboxChange} from "../../actions/ArticleViewer/ArticleActions";
import {find} from "underscore";

const mapStateToProps = (state, ownProps) => {
    let article = {
        articles: [null]
    };
    if (state.searchResults.fetchInProgressCalled) {
        article = find(state.searchResults.articles, (art) => {
            return art.id === ownProps.articleId;
        });
    }
    return {
        article,
        sources: state.articleViewer.sources,
        fetchInProgressCalled: state.searchResults.fetchInProgressCalled,
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
