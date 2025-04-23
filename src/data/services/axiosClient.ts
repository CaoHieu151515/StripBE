import axios from 'axios';
import { getJwtData } from '../auth/auth.service';
import { apiBaseUrl } from '@/utils/constants/shared.constant';

const axiosClient = axios.create({
  baseURL: apiBaseUrl,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 giây
});

// Interceptor để thêm token nếu cần
axiosClient.interceptors.request.use(
  (config) => {
    const accessToken = getJwtData()?.accessToken;
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
