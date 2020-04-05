import {baseImgUrl} from "../config/config";

export let outOfStockPrice = 2147483647;

export function array2Matrix(arr, lineLen = 6) {
    let matrix = [];
    let t = [];
    for (let e of arr) {
        if (t.length === lineLen) {
            matrix.push(t);
            t = [];
        } else {
            t.push(e);
        }
    }
    if (t.length !== 0) {
        matrix.push(t);
    }
    return matrix;
}

export function filterData(items, isLike = false) {
    return items.filter(e => e.isLike === isLike);
}

export function displaydData(items) {
    return array2Matrix(items);
}

export function chooseItem2UrlAndPic(website) {
    if (website === "mercari") {
        return item => {
            let url = item.url.includes("mercari.com") ? item.url : "https://www.mercari.com" + item.url;
            let picture = item.picture.includes("static.mercdn.net") ? item.picture : (baseImgUrl + item.picture);
            return ({url, picture});
        }
    } else if (website === "surugaya") {
        return item => {
            let url = item.url;
            let picture = item.picture.includes("/database/pics") ? item.picture : baseImgUrl + item.picture;
            return ({url, picture});
        }
    }
    return null;
}
