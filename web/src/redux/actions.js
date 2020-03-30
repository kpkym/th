/*
包含n个action creator
异步action
同步action
 */
import {INIT_MERCARI, UPDATE_MERCARI} from './action-types'
import {getAllMercari, updateMercari} from "api/index"


export function initMercariAction() {
    return dispatch => {
        getAllMercari().then(e => {
            dispatch({type: INIT_MERCARI, data: e.data.data});
        });
    };
}

export function updateMerciAction(data) {
    return dispatch => {
        updateMercari(data).then(() => dispatch({type: UPDATE_MERCARI, data}));
    };
}
