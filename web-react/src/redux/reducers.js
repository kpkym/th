/*
包含n个reducer函数: 根据老的state和指定的action返回一个新的state
 */
import {combineReducers} from 'redux'
import {DELETE, INIT, UPDATE, UPDATE_SINGLE, SEARCH} from './action-types';


function th(state = {items: [], isSearch: false}, action) {
    state = ({...state, isSearch: false});
    switch (action.type) {
        case INIT:
            return ({...state, items: action.data});
        case UPDATE:
            let idObjMap = new Map();
            action.data.forEach(e => idObjMap.set(e.id, e));

            return ({...state, items: state.items.map(e => {
                    if ([...idObjMap.keys()].includes(e.id)) {
                        return idObjMap.get(e.id);
                    } else {
                        return e;
                    }
                })});
        case DELETE:
            return ({...state, items: state.items.filter(e => !action.data.includes(e.id))});
        case SEARCH:
            return ({...state, items: action.data, isSearch: true});
        case UPDATE_SINGLE:
            let items = state.items;
            items[state.items.findIndex(e => e.id === action.data.id)] = action.data;
            return ({...state, items});
        default:
            return state
    }
    return state;
}

export default combineReducers({
    th
})

