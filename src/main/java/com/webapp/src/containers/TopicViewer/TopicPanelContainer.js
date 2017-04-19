/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import TopicPanelView from "../../components/TopicViewer/TopicPanelView";
import {articleClicked} from "../../actions/TopicViewer/TopicViewerActions";

const mapStateToProps = (state) => {
    return {
        articles: state.searchResults.articles
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleArticleClicked: (index) => {
            dispatch(articleClicked(index));
        }
    }
};

const TopicPanelContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(TopicPanelView);

export default TopicPanelContainer;
