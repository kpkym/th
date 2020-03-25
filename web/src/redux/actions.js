/*
包含n个action creator
异步action
同步action
 */
import {INIT_MERCARI, TRIGGER_ISSELLING, UPDATE_MERCARI} from './action-types'
import {getAllMercari, updateMercari} from "api/index"

// import data from 'test/data.json';

export function initMercariAction() {
    return dispatch => {
        getAllMercari().then(e => e.data.data)
            .then(data => {
                dispatch({type: INIT_MERCARI, data});
            });
        // dispatch({type: INIT_MERCARI, data: data.data})
    };
}


export function updateMerciAction(data) {
    console.log(data)
    return dispatch => {
        updateMercari(data).then(() => dispatch({type: UPDATE_MERCARI, data}));
        // dispatch(initMercariAction());
    };
}


export function triggerIsSellingAction() {
    return {
        type: TRIGGER_ISSELLING
    }
}

