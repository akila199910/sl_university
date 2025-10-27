"use client"
import React, { useEffect, useState } from 'react'
import { type User } from '@/types/users'

type PaginatedResponse = {
    content: User[];
    totalPages: number;
    totalElements: number;
    number: number;
    size: number;
}

const UsersPage = () => {
    const [users, setUsers] = useState<User[]>([]);
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
                const res = await fetch(`/api/admin/users?page=${page}&size=${size}`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' },
                    credentials: 'same-origin'
                });

                if (res.status === 401) {
                    window.location.href = '/login';
                    return;
                }

                const data: PaginatedResponse = await res.json().catch(() => ({} as PaginatedResponse));
                const list = Array.isArray(data?.content) ? data.content : [];

                if (mounted) {
                    setUsers(list);
                    setTotalPages(data.totalPages || 0);
                    setTotalElements(data.totalElements || 0);
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
    }, [page, size]);

    function gotoPage(n: number) {
        if (n < 0 || n >= totalPages) return;
        setPage(n);
    }

    function changeSize(newSize: number) {
        setSize(newSize);
        setPage(0);
    }

    const renderPageButtons = () => {
        if (totalPages <= 1) return null;
        const buttons = [] as React.ReactNode[];
        const start = Math.max(0, page - 2);
        const end = Math.min(totalPages - 1, start + 4);

        for (let i = start; i <= end; i++) {
            buttons.push(
                <button
                    key={i}
                    onClick={() => gotoPage(i)}
                    className={`px-3 py-2 leading-tight border ${i === page ? 'bg-blue-50 border-blue-300 text-blue-700' : 'bg-white border-gray-300 text-gray-600'}`}
                    aria-current={i === page}
                >
                    {i + 1}
                </button>
            );
        }

        return buttons;
    }

    return (
        <div className='bg-amber-100 m-2 p-2 rounded-2xl max-w-6xl mx-auto'>
            <h1 className='text-2xl font-semibold mb-4'>Users Management</h1>

            {loading && <p>Loading users…</p>}
            {error && <p className='text-red-600'>Error: {error}</p>}

            {!loading && !error && (
                <>
                    <div className='overflow-x-auto'>
                        <table className='table-auto w-full border-collapse border border-gray-300'>
                            <thead>
                                <tr className='bg-gray-200'>
                                    <th className='border border-gray-300 px-4 py-2'>#</th>
                                    <th className='border border-gray-300 px-4 py-2'>Name</th>
                                    <th className='border border-gray-300 px-4 py-2'>Email</th>
                                    <th className='border border-gray-300 px-4 py-2'>Role</th>
                                    <th className='border border-gray-300 px-4 py-2'>Contact</th>
                                    <th className='border border-gray-300 px-4 py-2'>Status</th>
                                    <th className='border border-gray-300 px-4 py-2'>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {users && users.length > 0 ? (
                                    users.map((u, index) => (
                                        <tr key={u.id}>
                                            <td className='border border-gray-300 px-4 py-2'>{page * size + index + 1}</td>
                                            <td className='border border-gray-300 px-4 py-2'>
                                                <div className='flex items-center gap-2'>
                                                    {u.profile && (u.profile as any).profileUrl ? (
                                                        <div>A</div>
                                                        // <img src={(u.profile as any).profileUrl} alt={u.name} className='w-8 h-8 rounded-full' />
                                                    ) : (
                                                        <div className='w-8 h-8 rounded-full bg-gray-300' />
                                                    )}
                                                    <span>{u.name}</span>
                                                </div>
                                            </td>
                                            <td className='border border-gray-300 px-4 py-2'>{u.email}</td>
                                            <td className='border border-gray-300 px-4 py-2'>{(u as any).role?.name ?? (u as any).role ?? '-'}</td>
                                            <td className='border border-gray-300 px-4 py-2'>{u.contactNumber}</td>
                                            <td className='border border-gray-300 px-4 py-2'>
                                                {u.status ? (
                                                    <span className='text-green-700'>Active</span>
                                                ) : (
                                                    <span className='text-gray-600'>Inactive</span>
                                                )}
                                            </td>
                                            <td className='border border-gray-300 px-4 py-2'>
                                                {u.viewUrl != null && (
                                                    <button
                                                        className='px-2 py-1 bg-green-500 text-white rounded mr-2 hover:bg-green-600'
                                                        onClick={() => alert(`Edit user ${u.id}`)}
                                                    >
                                                        View
                                                    </button>
                                                )}
                                                
                                                {u.editUrl != null && (
                                                    <button
                                                        className='px-2 py-1 bg-blue-500 text-white rounded mr-2 hover:bg-blue-600'
                                                        onClick={() => alert(`Edit user ${u.id}`)}
                                                    >
                                                        Edit
                                                    </button>
                                                )}

                                                {u.deleteUrl != null && (
                                                    <button
                                                    className='px-2 py-1 bg-red-500 text-white rounded hover:bg-red-600'
                                                    onClick={() => alert(`Delete user ${u.id}`)}
                                                >
                                                    Delete
                                                </button>
                                                )}
                                                
                                                
                                            </td>
                                        </tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td className='border border-gray-300 px-4 py-2' colSpan={6}>
                                            No users found.
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>

                    <div className='flex items-center justify-between mt-4'>
                        <div>
                            <button
                                onClick={() => gotoPage(page - 1)}
                                disabled={page <= 0}
                                className='px-3 py-2 mr-2 bg-white border border-gray-300 rounded disabled:opacity-50'
                            >Previous</button>

                            {renderPageButtons()}

                            <button
                                onClick={() => gotoPage(page + 1)}
                                disabled={page >= totalPages - 1}
                                className='px-3 py-2 ml-2 bg-white border border-gray-300 rounded disabled:opacity-50'
                            >Next</button>
                        </div>

                        <div className='flex items-center gap-4'>
                            <div className='text-sm text-gray-700'>
                                Page {page + 1} of {totalPages} — {totalElements} users
                            </div>

                            <label className='text-sm'>
                                <select value={size} onChange={(e) => changeSize(Number(e.target.value))} className='border rounded px-2 py-1'>
                                    <option value={10}>10</option>
                                    <option value={20}>20</option>
                                    <option value={50}>50</option>
                                </select>
                            </label>
                        </div>
                    </div>
                </>
            )}
        </div>
    )
}

export default UsersPage