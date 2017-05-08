import React from "react";
import ReactDOM from "react-dom";
import {createStore, compose, applyMiddleware} from "redux";
import {Provider} from "react-redux";
import {SearchBarJumbotron} from "./components/Home/SearchBarJumbotron";
import {Route, IndexRoute, browserHistory, Router} from "react-router";
import {syncHistoryWithStore, routerMiddleware} from "react-router-redux";
import {responsiveStoreEnhancer} from "redux-responsive";
import App from "./App";
import "babel-polyfill";
import thunk from "redux-thunk";
import NavBarContainer from "./containers/NavBarContainer";
import {SearchResultPage} from "./components/SearchResults/SearchResultPage";
import {SummaryEvaluation} from "./components/SummaryEvaluation/SummaryEvaluation";
import {TopicViewerPage} from "./components/TopicViewer/TopicViewer";
import {ArticleViewerPage} from "./components/ArticleViewer/ArticleViewerPage";
import {SubscriptionPage} from "./components/Subscriptions/Subscriptions";
//import {reducers} from './reducers'


const middleware = routerMiddleware(browserHistory);
const store = createStore(App, compose(responsiveStoreEnhancer, applyMiddleware(thunk, middleware)));
const rootEl = document.getElementById('root');
const history = syncHistoryWithStore(browserHistory, store);

class NavigationBar extends React.Component {
    render() {
        return (
            <div>
                <NavBarContainer/>
                {this.props.children}
            </div>
        );
    }
}

const render = () => {
    ReactDOM.render((
            <Provider store={store}>
                <Router history={history}>
                    <Route path="/" component={NavigationBar}>
                        <IndexRoute component={SearchBarJumbotron}/>
                        <Route path="searchResults/:searchTerm" component={SearchResultPage}/>
                        <Route path="summaryEvaluation" component={SummaryEvaluation}/>
                        <Route path="topic/:topicId" component={TopicViewerPage}/>
                        <Route path="topic/:topicId/article/:articleId" component={ArticleViewerPage}/>
                        <Route path="subscription/:userId" component={SubscriptionPage}/>
                    </Route>
                </Router>
            </Provider>
        ),
        rootEl
    );
};

render();

