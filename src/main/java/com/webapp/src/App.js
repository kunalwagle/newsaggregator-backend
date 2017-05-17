/**
 * Created by kunalwagle on 07/02/2017.
 */
import searchBar from "./reducers/Home/SearchBarReducers";
import summaryEvaluation from "./reducers/SummaryEvaluation/SummaryEvaluation";
import searchResults from "./reducers/SearchResults/SearchResultsReducers";
import topicViewer from "./reducers/TopicViewer/TopicViewerReducers";
import loggedIn from "./reducers/LoginModalReducers";
import articleViewer from "./reducers/ArticleViewer/ArticleReducers";
import settings from "./reducers/Settings/SettingsReducers";
import digest from "./reducers/Digest/DigestReducers";
import {routerReducer} from "react-router-redux";
import {responsiveStateReducer} from "redux-responsive";
import {combineReducers} from "redux";
import {reducer as toastrReducer} from "react-redux-toastr";

const App = combineReducers({
    searchBar,
    summaryEvaluation,
    searchResults,
    topicViewer,
    loggedIn,
    articleViewer,
    settings,
    digest,
    toastr: toastrReducer,
    routing: routerReducer,
    browser: responsiveStateReducer
});

export default App;