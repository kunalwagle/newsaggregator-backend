/**
 * Created by kunalwagle on 20/04/2017.
 */
import {connect} from "react-redux";
import {ArticleContent} from "../../components/ArticleViewer/ArticleContent";
import {find} from "underscore";
import {articleClicked} from "../../actions/SearchResults/SearchResultsActions";

const mapStateToProps = (state, ownProps) => {
    let article = state.articleViewer.article;
    let fetchInProgressCalled = state.articleViewer.fetchInProgressCalled;
    return {
        article,
        sources: state.articleViewer.sources,
        annotations: state.articleViewer.annotations,
        articleId: ownProps.articleId,
        fetchInProgressCalled
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