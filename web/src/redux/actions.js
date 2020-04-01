/*
包含n个action creator
异步action
同步action
 */
import {INIT, UPDATE} from './action-types'
import {getAllMercari, getAllSurugaya, updateMercari, updateSurugaya} from "api/index"
import {baseImgUrl} from "config/config";


export function initMercariAction() {
    return dispatch => {
        getAllMercari().then(e => {
            let items = e.data.data;
            let map = items.map(e => {
                e.url = e.url.includes("mercari.com") ? e.url : "https://www.mercari.com" + e.url;
                e.picture = e.picture.includes("static.mercdn.net") ?  e.picture : baseImgUrl + e.picture;
                return e;
            });
            dispatch({type: INIT, data: map});
        });
    };
}

export function updateMercariAction(data) {
    return dispatch => {
        updateMercari(data).then(() => dispatch({type: UPDATE, data}));
    };
}

export function initSurugayaAction() {
    return dispatch => {
        getAllSurugaya().then(e => {
            let items = e.data.data;
            let map = items.map(e => {
                e.picture = e.picture.includes("/database/pics") ?  e.picture : baseImgUrl + e.picture;
                return e;
            });
            dispatch({type: INIT, data: map});
        });
    };
}

export function updateSurugayaAction(data) {
    return dispatch => {
        updateSurugaya(data).then(() => dispatch({type: UPDATE, data}));
    };
}
