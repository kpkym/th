/*
包含n个reducer函数: 根据老的state和指定的action返回一个新的state
 */
import {combineReducers} from 'redux'
import {INIT, UPDATE} from './action-types';


function th(state = {items: []}, action) {
    switch (action.type) {
        case INIT:
            return ({...state, items: action.data});
        case UPDATE:
            let news = [...state.items];
            let number = news.findIndex(e => e.id === action.data.id);
            news[number] = action.data;
            return ({...state, items: news});
        default:
            return state
    }
    return state;
}

export default combineReducers({
    th
})

