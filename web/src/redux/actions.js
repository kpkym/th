/*
包含n个action creator
异步action
同步action
 */
import {TRIGGER_ISSELLING} from './action-types'


export function triggerIsSelling() {
    return {
        type: TRIGGER_ISSELLING
    }
}

