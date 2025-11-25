"use client";
type Permission = {
    id:number,
    name:string
}

export type Role = 
    { 
        id: number; 
        name : string,
        status: boolean,
        permissions: Permission[],
        viewUrl: string | null,
        editUrl: string | null,
        deleteUrl: string | null,
    };