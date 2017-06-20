/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import TopicPanelView from "../../components/TopicViewer/TopicPanelView";
import {articleClicked} from "../../actions/TopicViewer/TopicViewerActions";
import {viewClicked} from "../../actions/SearchResults/SearchResultsActions";

const mapStateToProps = (state, ownProps) => {
    let articles = state.searchResults.articles;
    let topicId = ownProps.topicId;
    let fetchInProgressCalled = state.searchResults.fetchInProgressCalled;
    const topic = ownProps.topic;
    if (ownProps.topic !== undefined) {
        topicId = ownProps.topic.id;
        articles = ownProps.topic.clusters;
        fetchInProgressCalled = state.searchResults.label === ownProps.topic.label;
        // fetchInProgressCalled = true;
    }
    return {
        topicId,
        articles,
        topic,
        articleCount: state.searchResults.articleCount,
        activePage: state.searchResults.activePage,
        fetchInProgress: state.searchResults.fetchInProgress,
        fetchInProgressCalled,
        mediaType: state.browser.mediaType
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleArticleClick: (event, topicId, articleId) => {
            event.preventDefault();
            dispatch(articleClicked(topicId, articleId));
        },
        handleReloadNeeded: (topicId, topic, page) => {
            dispatch(viewClicked(topicId, topic, page));
        }
    }
};

const TopicPanelContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(TopicPanelView);

export default TopicPanelContainer;
