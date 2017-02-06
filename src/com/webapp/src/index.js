import React from "react";
import ReactDOM from "react-dom";
import {createStore} from "redux";
import {SearchBar} from "./components/Home/SearchBar";
//import {reducers} from './reducers'

//const store = createStore(counter);
const rootEl = document.getElementById('root');

const render = () => ReactDOM.render(
    <SearchBar/>,
    rootEl
)

render()

