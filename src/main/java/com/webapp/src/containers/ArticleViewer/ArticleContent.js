/**
 * Created by kunalwagle on 20/04/2017.
 */
import {connect} from "react-redux";
import {ArticleContent} from "../../components/ArticleViewer/ArticleContent";
import {find} from "underscore";
import {articleClicked} from "../../actions/SearchResults/SearchResultsActions";

const mapStateToProps = (state, ownProps) => {
    let article = {};
    if (state.searchResults.fetchInProgressCalled) {
        article = find(state.searchResults.articles, (art) => {
            return art.id === ownProps.articleId;
        });
    }
    return {
        article,
        annotations: state.articleViewer.annotations,
        topicId: ownProps.topicId,
        fetchInProgressCalled: state.searchResults.fetchInProgressCalled
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleReloadNeeded: (topicId) => {
            dispatch(articleClicked(topicId));
        }
    }
};

const ArticleContentContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(ArticleContent);

export default ArticleContentContainer;