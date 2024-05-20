/**
 * 公共的请求方法
 */
import base from './base'
import axios from 'axios'

const api ={
     /**
      * 今日推荐 tuijian
      */
     getTuijian(){
         return axios.get(base.tuijian)
     }

}


export default api;