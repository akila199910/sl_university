import { AuthResponse, LoginDto, RegisterDto, User } from "@/types/auth";
import api from "./api";

export async function apiLogin(payload:LoginDto): Promise<AuthResponse> {
    const { data } = await api.post<AuthResponse>('/auth/login', payload);
    return data;
}

export async function apiRegister(payload:RegisterDto){
    return api.post("/auth/register", payload);
}

export async function refresh():Promise<AuthResponse> {
    const { data } = await api.post<AuthResponse>("/auth/refresh");
    return data;
}

export async function getMe():Promise<User> {
    // the server responds with the user object directly for /auth/me
    const { data } = await api.get<User>('/auth/me');
    console.log("getMe response data:", data);
    return data;
}

export async function apiLogout() {
    return api.post("/auth/logout");
}