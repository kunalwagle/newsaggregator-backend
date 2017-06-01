/**
 * Created by kunalwagle on 20/04/2017.
 */
import {connect} from "react-redux";
import {ArticleContent} from "../../components/ArticleViewer/ArticleContent";
import {find} from "underscore";
import {reloadArticle} from "../../actions/ArticleViewer/ArticleActions";

const mapStateToProps = (state, ownProps) => {
    let article = state.articleViewer.article;
    let fetchInProgressCalled = state.articleViewer.fetchInProgressCalled;
    return {
        article,
        sources: state.articleViewer.sources,
        annotations: state.articleViewer.annotations,
        articleId: ownProps.articleId,
        topicId: ownProps.topicId,
        fetchInProgressCalled
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleReloadNeeded: (topicId, articleId) => {
            dispatch(reloadArticle(articleId, topicId));
        }
    }
};

const ArticleContentContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(ArticleContent);

export default ArticleContentContainer;