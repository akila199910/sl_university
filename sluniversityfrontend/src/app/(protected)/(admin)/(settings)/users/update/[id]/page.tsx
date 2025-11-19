"use client"
import api from '@/app/lib/api';
import Link from 'next/link';
import { useParams, useRouter } from 'next/navigation';
import React, { useEffect, useState } from 'react'
import DammyUser from '../../../../../../images/dammyUser.png'
import Image from 'next/image';
import SomeWrong from '@/components/Ui/Helper/SomeWrong';
import PermissionCheckBox from '@/components/Ui/Form/PermissionCheckBox';
import Conform from '@/components/Ui/Helper/Conform';

type UserResponse = {
    id: number;
    email: string;
    firstName: string,
    lastName: string,
    roles: {
        id: number,
        name: string,
        select: boolean
    }[],
    contactNumber: string,
    status: boolean,
    profile: string,
    canUpdate: boolean
};
const page = () => {

    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [showSomethinWrong, setShowSomethinWrong] = useState(false);
    const [user, setUser] = useState<UserResponse | null>(null);
    const [roleIds, setRoleIds] = useState<number[]>([]);
    const [formData, setFormData] = useState<{ roleids: number[], status: boolean }>({
        roleids: [],
        status: false,
    });

    const [showConfirmModal, setShowConfirmModal]= useState<boolean>(false)
    const [isSubmitting, setIsSubmitting] = useState(false);
    const router = useRouter();
    
    const { id } = useParams();

    useEffect(() => {
        let mounted = true;

        async function load() {
            setLoading(true);
            setError(null);

            try {
                const res = await api.get(`/system_users/update/${id}`);

                if (mounted) {
                    const data = res.data.data;
                    console.log(data)

                    const userRes = {
                        id: data.id,
                        email: data.email,
                        firstName: data.firstName,
                        lastName: data.lastName,
                        roles: data.roles,
                        contactNumber: data.contactNumber,
                        status: data.status,
                        profile: data.profile,
                        canUpdate: data.canUpdate
                    }

                    setUser(userRes)
                    const selectedRoleIds = userRes.roles
                        .filter((r: any) => r.select)
                        .map((r: any) => r.id);
                    setRoleIds(selectedRoleIds);

                    setFormData({
                        roleids: selectedRoleIds,
                        status: userRes.status,
                    });
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
    }, [id]);

    const handleStatusChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const newStatus = e.target.checked;
        setFormData(prevFormData => ({
            ...prevFormData,
            status: newStatus
        }));
    };

    const handleRoleToggle = (id: number) => {
        setFormData(prevFormData => {
            const roleids = prevFormData.roleids;
            if (roleids.includes(id)) {
                return {
                    ...prevFormData,
                    roleids: roleids.filter(roleId => roleId !== id)
                };
            } else {
                return {
                    ...prevFormData,
                    roleids: [...roleids, id]
                };
            }
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
       
               e.preventDefault();
               
               setShowConfirmModal(true); 
            
           }

    const handleConfirmSubmit = async () => {

        setIsSubmitting(true);

        try {

            console.log(formData)
            const response = await api.put(`system_users/${id}`,{
                roleIds : formData.roleids,
                status : formData.status
            });
            
           router.push(`/users?success_update=true`);
            
        } catch (err: any) {

            if(err.status==500){

                setShowSomethinWrong(true)
            }
            setLoading(false);
            setIsSubmitting(false); 
            setShowConfirmModal(false);
        }
    };

    const isStatusChecked = formData.status;


    return (
        <div className=' m-2 p-2 rounded-2xl max-w-6xl mx-auto'>

            {loading && <p>Loading ...</p>}
            {error && <p className='text-red-600'>Error: {error}</p>}

            {!loading && !error &&
                <div className='space-y-2 bg-white shadow-md rounded-lg p-6'>

                    <div className='flex justify-between items-center'>
                        <h1 className='text-2xl font-semibold mb-4'>UPDATE USER</h1>
                        <div className="flex justify-end mb-2">
                            <Link href="/users" className="bg-blue-500 hover:bg-blue-600 text-white 
                            py-1 sm:py-2 px-2 sm:px-4 rounded-md transition cursor-pointer"

                            >
                                Back
                            </Link>
                        </div>
                    </div>

                    <form className=" flex flex-col" onSubmit={ handleSubmit }>
                        {
                            user?.profile == 'profile.png' ? (
                                <Image src={DammyUser} alt='dammyUser' className='mb-2 rounded-md w-30 h-30' />) : (<h1>No</h1>
                            )
                        }
                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-4 mb-2">
                            <div>
                                <dt className='text-sm font-medium text-gray-500'>User Name</dt>
                                <dd className='mt-1 text-xl font-semibold text-gray-900'>{user?.firstName} {user?.lastName}</dd>
                            </div>

                            <div>
                                <dt className='text-sm font-medium text-gray-500'>Inactive/Active</dt>
                                <dd className='mt-1'>
                                    <div>
                                        <div className='mt-1'>
                                            <label className="inline-flex items-center cursor-pointer bg-blue-500 rounded-sm py-0.5">
                                                <input
                                                    type="checkbox"
                                                    checked={isStatusChecked}
                                                    onChange={handleStatusChange}
                                                    className="sr-only peer"
                                                />
                                                <div className="relative w-9 h-5 bg-neutral-quaternary peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-brand-soft dark:peer-focus:ring-brand-soft rounded-full peer peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-buffer after:content-[''] after:absolute after:top-[2px] after:start-[2px] after:bg-white after:rounded-full after:h-4 after:w-4 after:transition-all peer-checked:bg-brand"></div>
                                            </label>
                                        </div>
                                    </div>
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
                                        user.roles.map((r) => {
                                            return (
                                                <PermissionCheckBox labelName={r.name} htmlForAndId={r.name + r.id} permissionId={r.id.toString()}
                                                    checked={roleIds.includes(r.id)} onChange={() => { handleRoleToggle(r.id) }}
                                                    key={r.id} />
                                            )

                                        })



                                    )
                                    : (

                                        <h1>No</h1>
                                    )
                            }
                        </div>
                        <div className='flex w-full justify-end my-4'>
                            <button type="submit" className=" text-white bg-blue-500 hover:bg-blue-600
                        font-medium rounded-md text-sm px-4 py-2.5 mr-4 cursor-pointer">Submit</button>
                        </div>
                    </form>
                </div>
            }

            {showConfirmModal && (
                <Conform
                    isLoading={isSubmitting}
                    onCancel={() => setShowConfirmModal(false)}
                    onConfirm={handleConfirmSubmit}
                    message="Are you sure you want to Update this role?"
                />
            )}
            {showSomethinWrong && (
                <SomeWrong url="/users"/>
            )}
        </div>
    )
}

export default page