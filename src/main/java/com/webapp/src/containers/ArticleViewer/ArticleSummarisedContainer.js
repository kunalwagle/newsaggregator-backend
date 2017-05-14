/**
 * Created by kunalwagle on 20/04/2017.
 */
import {connect} from "react-redux";
import {ArticlesSummarised} from "../../components/ArticleViewer/ArticlesSummarised";
import {annotationsChange, defaultCheckboxes, checkboxChange} from "../../actions/ArticleViewer/ArticleActions";
import {find, uniq, map} from "underscore";

const mapStateToProps = (state, ownProps) => {
    let article = {
        articles: [null]
    };
    let fetchInProgressCalled = state.searchResults.fetchInProgressCalled;
    let sources = state.articleViewer.sources;
    if (fetchInProgressCalled) {
        article = find(state.searchResults.articles, (art) => {
            return art.id === ownProps.articleId;
        });
        if (article == undefined) {
            fetchInProgressCalled = false;
        } else {
            let newSources = map(article.articles, (art) => {
                return art.source;
            });
            newSources = uniq(newSources);
            const sourceString = "[" + newSources.sort().toString() + "]";
            const oldSourceString = "[" + sources.sort().toString() + "]"
            if (sourceString !== oldSourceString) {
                sources = [];
            }

        }
    }
    return {
        article,
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
