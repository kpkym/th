/*
包含n个reducer函数: 根据老的state和指定的action返回一个新的state
 */
import {combineReducers} from 'redux'
import {INIT_MERCARI} from './action-types';


function mercaris(state = {items: []}, action) {
    switch (action.type) {
        case INIT_MERCARI:
            return ({...state, items: action.data});
        default:
            return state
    }
    return state;
}

export default combineReducers({
    mercaris
})

