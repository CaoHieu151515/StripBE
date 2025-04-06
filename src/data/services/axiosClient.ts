import { STORAGE } from '@/utils/constants/shared.constant';
import axios from 'axios';
import Cookie from 'js-cookie';

const axiosClient = axios.create({
  baseURL: 'https://stripbe-production.up.railway.app/api',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 giây
});

// Interceptor để thêm token nếu cần
axiosClient.interceptors.request.use(
  (config) => {
    const accessToken = Cookie.get(STORAGE.ACCESS_TOKEN);
    const token = localStorage.getItem('access_token');
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

// Interceptor để xử lý lỗi response
axiosClient.interceptors.response.use(
  (response) => response.data,
  (error) => {
    console.error('API Error:', error.response);
    return Promise.reject(error);
  },
);

export default axiosClient;
