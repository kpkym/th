import ajax from './ajax'

// 获取所有数据
export const getAllMercari = () => ajax('mercari/list');
export const updateMercari = (mercari) => ajax('mercari/update', mercari, "POST");
