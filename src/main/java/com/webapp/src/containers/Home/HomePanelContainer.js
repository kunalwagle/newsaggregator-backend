/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import HomePanelView from "../../components/Home/HomePanelView";
import {digestArticleClicked, reloadDigest} from "../../actions/Home/HomeActions";

const mapStateToProps = (state, ownProps) => {
    return {
        articles: state.home.articles,
        fetchInProgress: state.home.fetchInProgress,
        fetchInProgressCalled: state.home.fetchInProgressCalled,
        mediaType: state.browser.mediaType
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleArticleClick: (event, topicId, articleId) => {
            event.preventDefault();
            dispatch(digestArticleClicked(topicId, articleId));
        },
        handleReloadNeeded: () => {
            dispatch(reloadDigest());
        }
    }
};

const HomePanelContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(HomePanelView);

export default HomePanelContainer;
