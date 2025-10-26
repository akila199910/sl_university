"use client"
import React, { useEffect, useState } from 'react'
import { type User } from '@/types/users'

const UsersPage = () => {
    const [users, setUsers] = useState<User[] | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        let mounted = true;

        async function load() {
            setLoading(true);
            setError(null);
            try {
                const res = await fetch('/api/admin/users', {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' },
                    credentials: 'same-origin'
                });
                
                if (res.status === 401) {
                    window.location.href = '/login';
                    return;
                }
                
                const data = await res.json().catch(() => ({}));
                console.log(data)
                if (mounted) setUsers(data.data);

            } catch (err: any) {
                console.error('Failed to load users', err);
                if (mounted) setError(err?.message || 'Failed to load users');
            } finally {
                if (mounted) setLoading(false);
            }
        }

        load();
        return () => { mounted = false };
    }, []);

    return (
        <div className='bg-amber-100 m-2 p-2 rounded-2xl max-w-6xl mx-auto'>
            <h1 className='text-2xl font-semibold mb-4'>Admin — Users</h1>

            {loading && <p>Loading users…</p>}
            {error && <p className='text-red-600'>Error: {error}</p>}

            {!loading && !error && (
                <div className='overflow-x-auto'>
                    <table className='table-auto w-full border-collapse border border-gray-300'>
                        <thead>
                            <tr className='bg-gray-200'>
                                <th className='border border-gray-300 px-4 py-2'>Name</th>
                                <th className='border border-gray-300 px-4 py-2'>Email</th>
                                <th className='border border-gray-300 px-4 py-2'>Role</th>
                                <th className='border border-gray-300 px-4 py-2'>Contact</th>
                                <th className='border border-gray-300 px-4 py-2'>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {users && users.length > 0 ? (
                                users.map((u) => (
                                    <tr key={u.id}>
                                        <td className='border border-gray-300 px-4 py-2'>
                                            <div className='flex items-center gap-2'>
                                                {u.profile.profileUrl ? (
                                                    <div className='w-8 h-8 rounded-full' > A </div>
                                                ) : (
                                                    <div className='w-8 h-8 rounded-full bg-gray-300' />
                                                )}
                                                <span>{u.name}</span>
                                            </div>
                                        </td>
                                        <td className='border border-gray-300 px-4 py-2'>{u.email}</td>
                                        <td className='border border-gray-300 px-4 py-2'>{u.role.name}</td>
                                        <td className='border border-gray-300 px-4 py-2'>{u.contactNumber}</td>
                                        <td className='border border-gray-300 px-4 py-2'>
                                            {u.status ? (
                                                <span className='text-green-700'>Active</span>
                                            ) : (
                                                <span className='text-gray-600'>Inactive</span>
                                            )}
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td className='border border-gray-300 px-4 py-2' colSpan={5}>
                                        No users found.
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    )
}

export default UsersPage