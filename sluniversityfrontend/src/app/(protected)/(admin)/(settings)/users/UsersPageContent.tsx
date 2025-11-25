"use client";

import React, { useEffect, useState } from 'react'
import { type User } from '@/types/users'
import api from '@/app/lib/api'
import Link from 'next/link'
import { useRouter, useSearchParams } from 'next/navigation'
import AddRecord from '@/components/Ui/Helper/AddRecord'
import Image from 'next/image'
import viewBlackIcon from '../../../../../../public/view-black.svg'
import viewWhiteIcon from '../../../../../../public/view-white.svg'
import ediBlackIcon from '../../../../../../public/edit-black.svg'
import editWhiteIcon from '../../../../../../public/edit-white.svg'
import deleteBlackIcon from '../../../../../../public/delete-black.svg'
import deleteWhiteIcon from '../../../../../../public/delete-white.svg'

type Role = {
    id: number,
    name: string
}

type UsersResponse = {
    userPage: {
        content: User[];
        page: {
            number: number;
            size: number;
            totalElements: number;
            totalPages: number;
        }
    },
    availableRoles: Role[]
}

const UsersPageContent = () => {

    const [users, setUsers] = useState<User[]>([]);
    const [availableRoles, setAvailableRoles] = useState<Role[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [page, setPage] = useState<number>(0);
    const [size, setSize] = useState<number>(10);
    const [role, setRole] = useState<number | ''>('')
    const [totalPages, setTotalPages] = useState<number>(0);
    const [totalElements, setTotalElements] = useState<number>(0);
    const [recordAdd, setRecordAdd] = useState<boolean>(false);
    const [showSomethinWrong, setShowSomethinWrong] = useState(false);
    const [successMessage, setSuccessMessage] = useState<string>('')
    const params = useSearchParams();
    const router = useRouter();


    useEffect(() => {
        let mounted = true;

        async function load() {
            setLoading(true);
            setError(null);
            try {
                const res = await api.get(`/system_users?page=${page}&size=${size}&role=${role}`);
                const data: UsersResponse = res.data.data;
                const list = Array.isArray(data?.userPage.content) ? data.userPage.content : [];

                if (mounted) {
                    setUsers(list);
                    setTotalPages(data?.userPage?.page?.totalPages || 0);
                    setTotalElements(data.userPage?.page?.totalElements || 0);
                    setAvailableRoles(data.availableRoles)
                }
            } catch (err: any) {

                if (err.status == 500) {
                    setShowSomethinWrong(true)
                }
                console.log(err)
                if (mounted) setError(err?.message || 'Failed to load users');
            } finally {
                if (mounted) setLoading(false);
            }
        }

        load();
        return () => { mounted = false };
    }, [page, size, role]);

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

    if (recordAdd) {
        setTimeout(() => setRecordAdd(false), 5000);
    }
    useEffect(() => {
        if (params.get("success") === "true") {
            setSuccessMessage('New Record Added Succuessfully.')
            setRecordAdd(true)
            router.replace("/users", undefined);
        }
        if (params.get("success_update") === "true") {
            setSuccessMessage('Record Updated Succuessfully.')
            setRecordAdd(true)
            router.replace("/users", undefined);
        }

    }, [params]);

    return (
        <div className='bg-white shadow-md rounded-lg max-w-6xl mx-auto'>
            <h1 className='text-2xl font-semibold px-2'>Users Management</h1>

            {loading && <p>Loading users…</p>}
            {error && <p className='text-red-600'>Error: {error}</p>}



            {!loading && !error && (
                <>
                    {
                        true && (
                            <div className="flex justify-end mb-2">
                                <Link href="users/create" className="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-md transition"
                                >
                                    Add User
                                </Link>
                            </div>
                        )

                    }

                    {
                        recordAdd && <AddRecord message={successMessage} />
                    }
                    <div className='overflow-x-auto'>
                        <table className='table-auto w-full border-collapse border border-gray-300'>
                            <thead>
                                <tr className='bg-gray-200'>
                                    <th className='border border-gray-300 px-4 py-2'>#</th>
                                    <th className='border border-gray-300 px-4 py-2'>Name</th>
                                    <th className='border border-gray-300 px-4 py-2'>Email</th>
                                    <th className='border border-gray-300 px-4 py-2'>
                                        <select
                                            value={role}
                                            onChange={(e) => setRole(e.target.value ? Number(e.target.value) : '')}
                                            className="w-full p-1 rounded border border-gray-300"
                                        >
                                            <option value="">All Roles</option>
                                            {availableRoles.map((r) => (
                                                <option
                                                    key={r.id}
                                                    value={r.id}
                                                >
                                                    {r.name}
                                                </option>
                                            ))}
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
                                            <td className='border border-gray-300 px-4 py-2'>{u.roles.map((r) => r.name + " / ")}</td>
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
                                                        className=' bg-green-500 text-white rounded mr-1 hover:bg-green-600'
                                                        onClick={()=>{window.location.href=`/users/view/${u.id.toString()}`}}
                                                    >
                                                    <Image src={viewBlackIcon} alt="eye" />
                                                    </button>
                                                )}

                                                {u.editUrl != null && (
                                                    <button
                                                        className=' bg-blue-500 text-white rounded mr-1 hover:bg-blue-600'
                                                        onClick={()=>{window.location.href=`/users/update/${u.id.toString()}`}}
                                                    >
                                                    <Image src={ediBlackIcon} alt="pencil" />
                                                    </button>
                                                )}

                                                {u.deleteUrl != null && (
                                                    <button
                                                        className=' bg-red-500 text-white rounded hover:bg-red-600'
                                                        onClick={() => alert(`Delete user ${u.id}`)}
                                                    >
                                                        <Image src={deleteBlackIcon} alt="delete" />
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

export default UsersPageContent
