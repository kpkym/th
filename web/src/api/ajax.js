/*
能发送ajax请求的函数模块
函数的返回值是promise对象
 */
import axios from 'axios';
import {baseUrl} from "config/config";

export default function ajax(url, data = {}, type = 'GET') {
  url = baseUrl + url;
  if (type === 'GET') { // 发送GET请求
    // 拼请求参数串
    // data: {username: tom, password: 123}
    // paramStr: username=tom&password=123
    let paramStr = '';
    Object.keys(data).forEach(key => {
      paramStr += key + '=' + data[key] + '&';
    });
    if (paramStr) {
      paramStr = paramStr.substring(0, paramStr.length - 1);
    }
    // 使用axios发get请求
    return axios.get(url + (paramStr ? '?' + paramStr : ""));
  } else if (type === "POST") {
    // 使用axios发post请求
    return axios.post(url, data);
  } else if (type === "PUT") {
    // 使用axios发put请求
    return axios.put(url, data);
  } else if (type === "PATCH") {
    // 使用axios发patch请求
    return axios.patch(url, data);
  }
};



