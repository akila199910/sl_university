"use client"
import { type Role } from '@/types/role';
import React, { useEffect, useState } from 'react'

const RolePage = () => {

    const [roles, setRoles] = useState<Role[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [page, setPage] = useState<number>(0);
    const [size, setSize] = useState<number>(10);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [totalElements, setTotalElements] = useState<number>(0);


    useEffect(() => {
        let mounted = true;

        async function load() {
            setLoading(true);
            setError(null);
            try {
                const res = await fetch(`/api/admin/roles`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' },
                    credentials: 'same-origin'
                });

                // if (res.status === 401) {
                //     window.location.href = '/login';
                //     return;
                // }

                const data = await res.json().catch(() => ({}));

                // const list = Array.isArray(data?.userPage.content) ? data.userPage.content : [];


                if (mounted) {
                    // setRoles(list);
                    setTotalPages(data?.userPage?.page?.totalPages || 0);
                    setTotalElements(data.userPage?.page?.totalElements || 0);
                    console.log('Available Roles:', data);
                }
            } catch (err: any) {
                console.error('Failed to load users', err);
                if (mounted) setError(err?.message || 'Failed to load users');
            } finally {
                if (mounted) setLoading(false);
            }
        }

        load();
        return () => { mounted = false };
    }, [page, size, roles]);

    return (
        <div>RolePage</div>
    )
}

export default RolePage