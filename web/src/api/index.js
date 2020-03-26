import ajax from './ajax'

// 获取所有数据
export const getAllMercari = () => ajax('mercari/list');
// export const getAllMercari = () => new Promise(a => a({data:{data}}));

export const updateMercari = (mercari) => ajax('mercari/update', mercari, "POST");
