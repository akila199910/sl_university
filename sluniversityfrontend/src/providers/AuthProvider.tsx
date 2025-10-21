"use client";

import { apiLogin, apiLogout, apiRegister, getMe, refresh } from "@/lib/auth-client";
import { AuthResponse, User } from "@/types/auth";
import { tokenStore } from "@/utils/token-store";
import { createContext, useContext, useEffect, useState } from "react";

type Ctx = {
    user: User | null;
    accessToken: string | null;
    login : (email:string, password:string)=>Promise<void>;
    register : (email:string, password:string)=>Promise<void>;
    logout : ()=>Promise<void>;
    setAccessToken : (t:string|null) =>void;
}

const AuthContext = createContext<Ctx | null>(null);

export function AuthProvider({children}: {children: React.ReactNode}){
    const [user, setUser] = useState<User|null>(null);
    const [accessToken, setAccessTokenState] = useState<string | null>(null);

    const setAccessToken = (t:string|null)=>{
        tokenStore.set(t);
        setAccessTokenState(t);
    }

    const bootstrap = async()=>{
        try {
            const res: AuthResponse = (await refresh());
            console.log("Refresh response data:", res);
            const token = res?.data?.accessToken ?? null;
            setAccessToken(token);
            if(token){
                const me = await getMe();
                setUser(me);
                console.log("User data after bootstrap:", me);
            } else {
                setUser(null);
            }
        } catch (error) {
            setAccessToken(null);
            setUser(null);
        }
    }

    useEffect(()=>{bootstrap();},[]);

    const login = async (email:string, password:string)=>{
        const res = await apiLogin({email,password});
        const token = res?.data?.accessToken ?? null;
        setAccessToken(token);
        if(token){
            const me = await getMe();
            console.log("User data after login:", me);
            setUser(me);
        } else {
            throw new Error('Login did not return access token');
        }
    };

    const register = async (email:string, password:string)=>{
        await apiRegister({email,password});
        await login(email,password);
    }

    const logout = async()=>{
        await apiLogout();
        setAccessToken(null);
        setUser(null);
    }

    return(
        <AuthContext.Provider value={{user,accessToken,login,register,logout,setAccessToken}}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuthContext = ()=>{
    const ctx = useContext(AuthContext);
    if(!ctx) throw new Error("useAuthContext must be used within AuthProvider");
    return ctx;
}
