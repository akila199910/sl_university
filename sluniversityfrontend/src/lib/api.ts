import axios, { AxiosError, InternalAxiosRequestConfig } from "axios";
import { API_BASE_URL } from "./env";
import { tokenStore } from "@/utils/token-store";

const api = axios.create({
    baseURL: API_BASE_URL,
    withCredentials: true, //send refresh token cookie with every request
});

api.interceptors.request.use((config : InternalAxiosRequestConfig) => {
    const token = tokenStore.get();
    if(token) config.headers.Authorization = `Bearer ${token}`;
    return config;
});

let isRefreshing = false;
let pending : Array<(t:string)=>void> = [];

api.interceptors.response.use(
    (response) => response,
    async(error : AxiosError) => {
        const oraginal : any = error.config;
        if(error.response?.status === 401 && !oraginal._isRetry) {
            oraginal._isRetry = true;
            if(!isRefreshing){
                isRefreshing = true;
                try {
                    // backend returns: { message, success, data: { accessToken, tokenType } }
                    const res: any = await api.post('/auth/refresh');
                    const accessToken = res?.data?.data?.accessToken ?? res?.data?.accessToken ?? null;
                    tokenStore.set(accessToken);
                    pending.forEach((fn)=>fn(accessToken));
                    pending = [];
                } catch (error) {
                    pending = [];
                    tokenStore.clear();
                    throw error;
                }finally{
                    isRefreshing = false;
                }
            }
            return new Promise((resolve) => {
                pending.push((token : string)=>{
                    oraginal.headers.Authorization = `Bearer ${token}`;
                    resolve(api(oraginal));
                });
            });
    }
    return Promise.reject(error);
});

export default api;