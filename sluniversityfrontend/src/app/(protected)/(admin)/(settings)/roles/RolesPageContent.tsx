'use client';

import React, { useState, useEffect } from 'react'
import Link from 'next/link'
import { useSearchParams, useRouter } from 'next/navigation'
import Image from 'next/image'
import api from '@/app/lib/api'
import AddRecord from '@/components/Ui/Helper/AddRecord'
import viewBlackIcon from '../../../../../../public/view-black.svg'
import ediBlackIcon from '../../../../../../public/edit-black.svg'
import deleteBlackIcon from '../../../../../../public/delete-black.svg'
import { type Role } from '@/types/role'

type RoleResponse = {
    content: Role[];
    page: {
        number: number;
        size: number;
        totalElements: number;
        totalPages: number;
    }
    canAdd: boolean;
}

const RolesPageContent = () => {
    const [roles, setRoles] = useState<Role[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [page, setPage] = useState<number>(0);
    const [size, setSize] = useState<number>(10);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [totalElements, setTotalElements] = useState<number>(0);
    const [canAdd, setCanAdd] = useState<boolean>(false);
    const [recordAdd, setRecordAdd] = useState<boolean>(false);
    const [successMessage, setSuccessMessage] = useState<string>('')
    const params = useSearchParams();
    const router = useRouter();


    useEffect(() => {
        let mounted = true;

        async function load() {
            setLoading(true);
            setError(null);

            try {
                const res = await api.get(`/roles?page=${page}&size=${size}`);
                const data: RoleResponse = res.data.data;
                const list = Array.isArray(data?.content) ? data.content : [];


                if (mounted) {
                    setRoles(list);
                    setTotalPages(data?.page?.totalPages || 0);
                    setTotalElements(data?.page?.totalElements || 0);
                    setCanAdd(res.data?.canAdd || false);
                }
            } catch (err: any) {
                if(err.status==500){
                    
                }
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

    const clickEdit = (id:string)=>{
        window.location.href=`/roles/update/${id}`;
    }

    if(recordAdd){
        setTimeout(() => setRecordAdd(false), 5000);
    }
    useEffect(() => {
        if (params.get("success") === "true") {
            setSuccessMessage('New Record Added Succuessfully.')
            setRecordAdd(true)
            router.replace("/roles", undefined); 
        }
        if (params.get("success_update") === "true") {
            setSuccessMessage('Record Updated Succuessfully.')
            setRecordAdd(true)
            router.replace("/roles", undefined); 
        }

    }, [params]);
     
    return (
        <div className='bg-white shadow-md rounded-lg max-w-6xl mx-auto'>
            <h1 className='text-2xl font-semibold px-2'>Roles Management</h1>

            {loading && <p>Loading Roles...</p>}
            {error && <p className='text-red-600'>Error: {error}</p>}



            {!loading && !error && (
                <>
                    {
                        canAdd && (
                            <div className="flex justify-end mb-2">
                                <Link href="roles/create" className="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-md transition"

                                >
                                    Add Role
                                </Link>
                            </div>
                        )
                    }
                    {
                        recordAdd && <AddRecord message={successMessage}/>
                    }
                    <div className='overflow-x-auto'>
                        <table className='table-auto w-full border-collapse border border-gray-300'>
                            <thead>
                                <tr className='bg-gray-200'>
                                    <th className='border border-gray-300 px-4 py-2'>#</th>
                                    <th className='border border-gray-300 px-4 py-2'>Name</th>
                                    <th className='border border-gray-300 px-4 py-2'>Permissions</th>
                                    <th className='border border-gray-300 px-4 py-2'>Status</th>
                                    <th className='border border-gray-300 px-4 py-2'>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {roles && roles.length > 0 ? (
                                    roles.map((r, index) => (
                                        <tr key={r.id}>
                                            <td className='border border-gray-300 px-4 py-2'>{page * size + index + 1}</td>
                                            <td className='border border-gray-300 px-4 py-2'>{r.name}</td>
                                            <td className='border border-gray-300 px-4 py-2'>{r.permissions.map((p) => {
                                                return (<span key={p.id} className="inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2 mb-2">{p.name}</span>);
                                            })}</td>
                                            <td className='border border-gray-300 px-4 py-2'>
                                                {r.status ? (
                                                    <span className='text-green-700 bg-green-300 px-2 rounded-sm py-1'>Active</span>
                                                ) : (
                                                    <span className='text-red-700 bg-red-300 px-2 rounded-sm py-1'>Inactive</span>
                                                )}
                                            </td>
                                            <td className='border border-gray-300 px-4 py-2'>

                                                {r.viewUrl != null && (
                                                    <button
                                                        className=' bg-green-500 text-white rounded mr-1 hover:bg-green-600'
                                                        onClick={()=>{window.location.href=`/roles/view/${r.id.toString()}`}}
                                                    >
                                                    <Image src={viewBlackIcon} alt="eye" />
                                                    </button>
                                                )}

                                                {r.editUrl != null && (
                                                    <button
                                                        className=' bg-blue-500 text-white rounded mr-1 hover:bg-blue-600'
                                                        onClick={(e)=>{
                                                            e.preventDefault();
                                                            clickEdit(r.id.toString());
                                                        }}
                                                    >
                                                    <Image src={ediBlackIcon} alt="pencil" />
                                                    </button>
                                                )}

                                                {r.deleteUrl != null && (
                                                    <button
                                                        className=' bg-red-500 text-white rounded hover:bg-red-600'
                                                        onClick={() => alert(`Delete user ${r.id}`)}
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

export default RolesPageContent
