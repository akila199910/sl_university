import axios from "axios";

const api = axios.create({ 
  baseURL: process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080",
  timeout: 5000,
  headers: { "Content-Type": "application/json" },
  withCredentials: true,
});

// A flag to prevent multiple refresh calls at the same time
let isRefreshing = false;
// A queue to hold requests that failed while refreshing
let failedQueue: Array<{ resolve: (value: any) => void; reject: (reason?: any) => void; }> = [];

// This function processes all requests in the queue
const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

// Response Interceptor
api.interceptors.response.use(
  (response) => {
    // Any status code that lies within the range of 2xx causes this function to trigger
    return response;
  },
  async (error) => {
    // Any status codes that fall outside the range of 2xx cause this function to trigger
    const originalRequest = error.config;
    console.log("API Error:", originalRequest);

    // Check if the error is 401 (Unauthorized) and it's not a retry request
    if (error.response?.status === 401 && !originalRequest._retry) {
      console.log("401 detected, attempting token refresh...");
      if (isRefreshing) {
        // If we are already refreshing, push this request to the queue
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
        .then(() => api(originalRequest)) // Retry the original request after refresh
        .catch(err => Promise.reject(err));
      }

      // This is the first 401. Set the retry flag and start refreshing.
      originalRequest._retry = true;
      isRefreshing = true;

      try {
        
        await api.post('/auth/refresh');

        // Refresh was successful. Process the queue and retry the original request.
        processQueue(null, 'new_access_token_not_needed_by_js'); 
        return api(originalRequest);

      } catch (refreshError: any) {
        // Refresh FAILED.
        processQueue(refreshError, null);
        
        // This means the refresh token is also bad.
        // We must log the user out.
        console.error("Session expired. Logging out.");

        // Here you would trigger a logout (e.g., clear user state)
        // and redirect to the login page.
        // window.location.href = '/login'; 
        
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    // For any other error, just reject it
    return Promise.reject(error);
  }
);

export default api;