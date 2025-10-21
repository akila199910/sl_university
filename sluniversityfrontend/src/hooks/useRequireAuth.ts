"use client"

import { useRouter } from "next/router";
import { useAuth } from "./useAuth"
import { useEffect } from "react";

export function useRequireAuth(){
    const { user } = useAuth();
    const router = useRouter();

    useEffect(()=>{
        const t = setTimeout(()=>{
            if(user == null) router.replace("/login");
        },0);
        return ()=>clearTimeout(t);
    },[user,router]);

    return {user}
}