/*
包含n个action creator
异步action
同步action
 */
import {INIT, UPDATE} from './action-types'
import {getAllMercari, getAllSurugaya, updateMercari, updateSurugaya} from "api/index"


export function initMercariAction() {
    return dispatch => {
        return getAllMercari().then(e => dispatch({type: INIT, data: e.data.data}));
    };
}

export function updateMercariAction(data) {
    return dispatch => {
        return updateMercari(data).then(() => dispatch({type: UPDATE, data}));
    };
}

export function initSurugayaAction() {
    return dispatch => {
        return getAllSurugaya().then(e => {
            dispatch({type: INIT, data: e.data.data})
        });
    }
}

export function updateSurugayaAction(data) {
    return dispatch => {
        return updateSurugaya(data).then(() => dispatch({type: UPDATE, data}));
    };
}
