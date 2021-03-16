/*
包含n个action creator
异步action
同步action
 */
import {INIT, UPDATE, UPDATE_SINGLE, DELETE, SEARCH} from './action-types'
import {getAllMercari, getAllSurugaya, updateMercari, updateSurugaya, delMercari, delSurugaya, flushSurugayaImg} from "api/index"

function convert2Arr(data) {
    return Array.isArray(data) ? data : [data];
}

function updateDate(arr, func) {
    return arr.map(e => func(e));
}

export function initMercariAction() {
    return dispatch => {
        return getAllMercari().then(e => dispatch({type: INIT, data: e.data.data}));
    };
}

export function updateMercariAction(data, func) {
    return dispatch => {
        let updatedData = updateDate(convert2Arr(data), func);
        return updateMercari(updatedData).then(() => dispatch({type: UPDATE, data: updatedData}));
    };
}

export function delMercariAction(data) {
    let ids = convert2Arr(data);
    return dispatch => {
        return delMercari(ids).then(() => dispatch({type: DELETE, data:ids}));
    };
}

export function searchMercariAction(keyword) {
    return dispatch => {
        return getAllMercari(keyword).then(e => {
            dispatch({type: SEARCH, data: e.data.data})
        });
    }
}

export function initSurugayaAction() {
    return dispatch => {
        return getAllSurugaya().then(e => {
            dispatch({type: INIT, data: e.data.data})
        });
    }
}

export function updateSurugayaAction(data, func) {
    return dispatch => {
        let updatedData = updateDate(convert2Arr(data), func);
        return updateSurugaya(updatedData).then(() => dispatch({type: UPDATE, data: updatedData}));
    };
}

export function delSurugayaAction(data) {
    let ids = convert2Arr(data)
    return dispatch => {
        return delSurugaya(ids).then(() => dispatch({type: DELETE, data:ids}));
    };
}


export function searchSurugayaAction(keyword) {
    return dispatch => {
        return getAllSurugaya(keyword).then(e => {
            dispatch({type: SEARCH, data: e.data.data})
        });
    }
}

export function flushImgSurugayaAction(id) {
    return dispatch => {
        return flushSurugayaImg(id).then(e => {
            dispatch({type: UPDATE_SINGLE, data: e.data.data})
        });
    }
}