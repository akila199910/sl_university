"use client"
import api from '@/app/lib/api';
import { useParams } from 'next/navigation';
import React, { useEffect, useState } from 'react'

type EoleResponseResponse = {
    topic: string,
    actions: {
        id: number,
        action: string,
        select : boolean
    }[],
    roleName: string
}

const page = () => {

    const [permissions, setPermissions] = useState<EoleResponseResponse[]>([])
    const [roleName, setRoleName] = useState<string>("");
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const { id } = useParams();

     useEffect(() => {
        let mounted = true;

        async function load() {
            setLoading(true);
            setError(null);

            try {
                const res = await api.get(`/roles/${id}`);
                
                if (mounted) {
                    setRoleName(res.data?.data?.roleName || "");
                    const permissionsData = res.data?.data?.permissions || [];
                    console.log(permissionsData)
                    setPermissions(permissionsData)
                }

            } catch (err: any) {
                console.error('Failed to load roles', err);
                if (mounted) setError(err?.message || 'Failed to load users');
            } finally {
                if (mounted) setLoading(false);
            }
        }

        load();
        return () => { mounted = false };
    }, []);
    
    return (
        <div className=' m-2 p-2 rounded-2xl max-w-6xl mx-auto'>
            <h1 className='text-2xl font-semibold mb-4'>VIEW ROLE</h1>

            {loading && <p>Loading ...</p>}
            {error && <p className='text-red-600'>Error: {error}</p>}

            {!loading && !error &&
                 <div className='space-y-6'>
                
                <div className="bg-white shadow-md rounded-lg p-6">
                    <dl className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4">
                        <div>
                            <dt className='text-sm font-medium text-gray-500'>Role Name</dt>
                            <dd className='mt-1 text-xl font-semibold text-gray-900'>{roleName}</dd>
                        </div>

                        <div>
                            <dt className='text-sm font-medium text-gray-500'>Status</dt>
                            <dd className='mt-1'>
                                <span className="inline-flex items-center rounded-full bg-green-100 px-3 py-0.5 text-sm font-medium text-green-800">
                                    Active
                                </span>
                            </dd>
                        </div>
                    </dl>
                </div>

                {/* --- Permissions Card --- */}
                <div className="bg-white shadow-md rounded-lg">
                    <h2 className="text-xl font-semibold p-6 border-b border-gray-200 text-gray-900">Permissions</h2>
                    <div className="space-y-6 p-6">
                        {permissions.length > 0 ? (
                            permissions.map((permission) => (
                                <div key={permission.topic} className="pt-4 first:pt-0">
                                    <h3 className="text-lg font-medium text-indigo-700 mb-3">{permission.topic}</h3>
                                    
                                    {/* Responsive grid for actions */}
                                    <div className='grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3'>
                                        {permission.actions.map((ac) => (
                                            <div 
                                                key={ac.id} 
                                                className={`flex items-center p-3 rounded-md border
                                                    ${ac.select 
                                                        ? 'bg-green-50 border-green-200' 
                                                        : 'bg-gray-50 border-gray-200'
                                                    }`}
                                            >
                                                {/* {ac.select ? <CheckIcon /> : <XIcon />} */}
                                                <span className={`ml-2.5 text-sm font-medium ${ac.select ? 'text-green-900' : 'text-gray-700'}`}>
                                                    {ac.action}
                                                </span>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            ))
                        ) : (
                            <p className="text-gray-500 text-center py-4">No permissions are assigned to this role.</p>
                        )}
                    </div>
                </div>
            </div>
            }
        </div>
    )
}

export default page