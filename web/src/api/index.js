import ajax from './ajax'

// 获取所有数据
export const getAllMercari = () => ajax('mercari/list');
export const updateMercari = (mercari) => ajax('mercari/update', mercari, "POST");
export const crawlerMercari = () => ajax('mercari/start');

export const getAllSurugaya = () => ajax('surugaya/list');
export const updateSurugaya = (surugaya) => ajax('surugaya/update', surugaya, "POST");
export const crawlerSurugaya = () => ajax('surugaya/start');

