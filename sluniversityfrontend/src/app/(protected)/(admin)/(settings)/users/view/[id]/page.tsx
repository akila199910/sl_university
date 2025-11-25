"use client";

import api from '@/app/lib/api';
import SomeWrong from '@/components/Ui/Helper/SomeWrong';
import Link from 'next/link';
import { useParams } from 'next/navigation';
import React, { useEffect, useState } from 'react'
import DammyUser from '../../../../../../images/dammyUser.png'
import Image from 'next/image';

type UserResponse = {
    id: number;
    email: string;
    name: string,
    roles: {
        id: number,
        name: string
    }[],
    contactNumber: string,
    status: boolean,
    profile: string
};

const page = () => {

    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [showSomethinWrong, setShowSomethinWrong] = useState(false);
    const [user, setUser] = useState<UserResponse | null>(null)
    const { id } = useParams();

    useEffect(() => {
        let mounted = true;

        async function load() {
            setLoading(true);
            setError(null);

            try {
                const res = await api.get(`/system_users/${id}`);

                if (mounted) {
                    console.log(res.data.data)

                    const data = res.data.data;

                    const userRes = {
                        id: data.id,
                        email: data.email,
                        name: data.name,
                        roles: data.roles,
                        contactNumber: data.contactNumber,
                        status: data.status,
                        profile: data.profile.profileUrl
                    }

                    setUser(userRes)
                }

            } catch (err: any) {
                if (err.status == 500) {
                    setShowSomethinWrong(true)
                } else {
                    if (mounted) setError(err?.message || 'Failed to load users');
                }

            } finally {
                if (mounted) setLoading(false);
            }
        }

        load();
        return () => { mounted = false };
    }, []);

    return (
        <div className=' m-2 p-2 rounded-2xl max-w-6xl mx-auto'>

            {loading && <p>Loading ...</p>}
            {error && <p className='text-red-600'>Error: {error}</p>}

            {!loading && !error &&
                <div className='space-y-2 bg-white shadow-md rounded-lg p-6'>

                    <div className='flex justify-between items-center'>
                        <h1 className='text-2xl font-semibold mb-4'>VIEW USER</h1>
                        <div className="flex justify-end mb-2">
                            <Link href="/users" className="bg-blue-500 hover:bg-blue-600 text-white 
                            py-1 sm:py-2 px-2 sm:px-4 rounded-md transition cursor-pointer"

                            >
                                Back
                            </Link>
                        </div>
                    </div>

                    <div className=" flex flex-col">

                        {
                            user?.profile == 'profile.png' ? (
                                <Image src={DammyUser} alt='dammyUser' className='mb-2 rounded-md w-30 h-30' />) : (<h1>No</h1>
                            )
                        }
                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-4 mb-2">
                            <div>
                                <dt className='text-sm font-medium text-gray-500'>User Name</dt>
                                <dd className='mt-1 text-xl font-semibold text-gray-900'>{user?.name}</dd>
                            </div>

                            <div>
                                <dt className='text-sm font-medium text-gray-500'>Status</dt>
                                <dd className='mt-1'>
                                    {
                                        user?.status == true ? (<span className="inline-flex items-center rounded-full bg-green-100 px-3 py-0.5 text-sm font-medium text-green-800">
                                            Active
                                        </span>) :

                                            (<span className="inline-flex items-center rounded-full bg-red-100 px-3 py-0.5 text-sm font-medium text-red-800">
                                                Inactive
                                            </span>)
                                    }

                                </dd>
                            </div>
                            <div>
                                <dt className='text-sm font-medium text-gray-500'>User Email</dt>
                                <dd className='mt-1 text-xl font-semibold text-gray-900 text-wrap break-all'>{user?.email}</dd>
                            </div>

                            <div>
                                <dt className='text-sm font-medium text-gray-500'>Contact Number</dt>
                                <dd className='mt-1 text-xl font-semibold text-gray-900'>{user?.contactNumber}</dd>
                            </div>
                        </div>

                        <hr />

                        <h1 className='mt-1 text-xl font-semibold text-gray-900 mb-3'>User Roles</h1>

                        <div className='grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 w-full'>


                            {
                                user && user.roles && user.roles.length !== 0
                                    ? (
                                        user.roles.map((u) => {
                                            return (
                                                <p key={u.id}>{u.name}</p>
                                            )
                                        })
                                    )
                                    : (

                                        <h1>No</h1>
                                    )
                            }
                        </div>

                    </div>
                </div>
            }

            {showSomethinWrong && (
                <SomeWrong url="/users" />
            )}
        </div>
    )
}

export default page