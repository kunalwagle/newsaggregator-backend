import React from "react";
import ReactDOM from "react-dom";
import {createStore, compose, applyMiddleware} from "redux";
import {Provider} from "react-redux";
import {SearchBarJumbotron} from "./components/Home/SearchBarJumbotron";
import {Router, Route, browserHistory} from "react-router";
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

const store = createStore(App, compose(applyMiddleware(thunk)));
const rootEl = document.getElementById('root');
const navBar = document.getElementById('navbar');

const render = () => {
    ReactDOM.render((
            <Provider store={store}>
                <NavBarContainer/>
            </Provider>
        ),
        navBar
    );
    ReactDOM.render((
            <Provider store={store}>
                <Router history={browserHistory}>
                    <Route path="/" component={SearchBarJumbotron}/>
                    <Route path="/searchResults" component={SearchResultPage}/>
                    <Route path="/summaryEvaluation" component={SummaryEvaluation}/>
                    <Route path="/topic" component={TopicViewerPage}/>
                    <Route path="/article" component={ArticleViewerPage}/>
                    <Route path="/subscriptions" component={SubscriptionPage}/>
                </Router>
            </Provider>
        ),
        rootEl
    );
};

render();

