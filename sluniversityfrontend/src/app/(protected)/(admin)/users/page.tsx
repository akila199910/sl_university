"use client"
import React, { useEffect, useState } from 'react'
import { type User } from '@/types/users'
import {type Role } from '@/types/role';

const UsersPage = () => {
    const [users, setUsers] = useState<User[] | null>(null);
    const [roles, setRoles] = useState<Role[] | null>(null)
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
                if (mounted) setUsers(data.content);

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
            <h1 className='text-2xl font-semibold mb-4'>Users Management</h1>

            {loading && <p>Loading users…</p>}
            {error && <p className='text-red-600'>Error: {error}</p>}

            {!loading && !error && (
                <div className='overflow-x-auto'>
                    <table className='table-auto w-full border-collapse border border-gray-300'>
                        <thead>
                            <tr className='bg-gray-200'>
                                <th className='border border-gray-300 px-4 py-2'>#</th>
                                <th className='border border-gray-300 px-4 py-2'>Name</th>
                                <th className='border border-gray-300 px-4 py-2'>Email</th>
                                <th className='border border-gray-300 px-4 py-2'>
                                    <select className='focus:outline-none px-2'>
                                        <option value="">All Role</option>
                                        <option value="">Admin</option>
                                        <option value="">User</option>
                                        <option value="">Client</option>
                                    </select>
                                </th>
                                <th className='border border-gray-300 px-4 py-2'>Contact</th>
                                <th className='border border-gray-300 px-4 py-2'>Status</th>
                                <th className='border border-gray-300 px-4 py-2'>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {users && users.length > 0 ? (
                                users.map((u, index) => (
                                    <tr key={u.id}>
                                        <td className='border border-gray-300 px-4 py-2'>{index + 1}</td>
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
                    <div className="flex justify-center mt-4">
                        <nav aria-label="Page navigation example">
                            <ul className="inline-flex items-center -space-x-px">
                                <li>
                                    <a href="#" className="px-3 py-2 ml-0 leading-tight text-gray-500 bg-white border border-gray-300 rounded-l-lg hover:bg-gray-100 hover:text-gray-700">Previous</a>
                                </li>
                                <li>
                                    <a href="#" className="px-3 py-2 leading-tight text-blue-600 bg-blue-50 border border-blue-300 hover:bg-blue-100 hover:text-blue-700">1</a>
                                </li>
                                <li>
                                    <a href="#" className="px-3 py-2 leading-tight text-gray-500 bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700">2</a>
                                </li>
                                <li>
                                    <a href="#" className="px-3 py-2 leading-tight text-gray-500 bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700">3</a>
                                </li>
                                <li>
                                    <a href="#" className="px-3 py-2 leading-tight text-gray-500 bg-white border border-gray-300 rounded-r-lg hover:bg-gray-100 hover:text-gray-700">Next</a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            )}
        </div>
    )
}

export default UsersPage