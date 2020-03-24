/*
包含n个reducer函数: 根据老的state和指定的action返回一个新的state
 */
import {combineReducers} from 'redux'

import {TRIGGER_ISSELLING} from './action-types';

import data from 'test/data.json';


function mercaris(state = {items: data.data, isSelling: true}, action) {
    switch (action.type) {
        case TRIGGER_ISSELLING:
            return ({...state, isSelling: !state.isSelling});
        default:
            return state
    }
    return state;
}

export default combineReducers({
    mercaris
})

