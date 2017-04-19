/**
 * Created by kunalwagle on 20/04/2017.
 */
import {connect} from "react-redux";
import {ArticleContent} from "../../components/ArticleViewer/ArticleContent";

const mapStateToProps = (state) => {
    return {
        article: state.topicViewer.article
    }
};

const mapDispatchToProps = () => {
    return {}
};

const ArticleContentContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(ArticleContent);

export default ArticleContentContainer;