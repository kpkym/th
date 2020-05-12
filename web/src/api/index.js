import ajax from './ajax'

export const getAllMercari = (keyword) => ajax('mercari/list?' + (keyword ? `keyword=${keyword}` : ''));
export const updateMercari = (mercari) => ajax('mercari', mercari, "PUT");
export const delMercari = (ids) => ajax('mercari', ids, "PATCH");
export const crawlerMercari = () => ajax('mercari/start');

export const getAllSurugaya = (keyword) => ajax('surugaya/list?' + (keyword ? `keyword=${keyword}` : ''));
export const updateSurugaya = (surugaya) => ajax('surugaya', surugaya, "PUT");
export const delSurugaya = (ids) => ajax('surugaya', ids, "PATCH");
export const crawlerSurugaya = () => ajax('surugaya/start');

