import ajax from './ajax'

// 获取所有数据
export const getAllMercari = () => ajax('mercari/list');
