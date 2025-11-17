"use client"
import api from '@/app/lib/api'
import PermissionCheckBox from '@/components/Ui/Form/PermissionCheckBox'
import TextFormInput from '@/components/Ui/Form/TextFormInput'
import React, { useEffect, useState } from 'react'

type PermissionResponse = {
    topic: string,
    actions: {
        id: number,
        action: string
    }[]
}
const AddRole = () => {

    const [permissions, setPermissions] = useState<PermissionResponse[]>([])
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [selectedPermissions, setSelectedPermissions] = useState<number[]>([]);
    const [roleName, setRoleName] = useState<string>("");

    useEffect(
        () => {
            const fetchPermissions = async () => {

                setLoading(true);
                setPermissions([]);
                try {
                    const response = await api.get("roles/create");
                    setPermissions(response.data.data.permissions);
                    setLoading(false);

                } catch (error) {
                    setError(error instanceof Error ? error.message : "An unknown error occurred");
                    setLoading(false);
                }
                finally {
                    setLoading(false);
                }

            }
            fetchPermissions()
        }, [])

        const handleSubmit = async (e:React.FormEvent) => {
            e.preventDefault();
            setLoading(true);
            setError(null);
            try {

                const response = await api.post("roles", {
                    name: roleName,
                    permissions: selectedPermissions,
                    status: true
                });

                setLoading(false);

                //here should be show success alert
                window.location.href = "/roles"; //redirect to roles list page after successful creation

                console.log("Role created successfully:", response.data);
                
            } catch (error) {

                console.error("Error creating role:", error);
                setError(error instanceof Error ? error.message : "An unknown error occurred");
                
            }
        }

    return (
        <div className='bg-amber-100 m-2 p-2 rounded-2xl max-w-6xl mx-auto'>
            <h1 className='text-2xl font-semibold mb-4'>ADD NEW ROLE</h1>

            {loading && <p>Loading ...</p>}
            {error && <p className='text-red-600'>Error: {error}</p>}

            {!loading && !error &&
                <>
                    <form onSubmit={handleSubmit}>
                        <div className="grid gap-6 mb-6 md:grid-cols-2 md:w-2/3">
                            <TextFormInput htmlForAndId='role_name' labelNaame='Role Name' 
                            isRequired={true} placeHolder='Role Name'
                            onChange={(e:React.ChangeEvent<HTMLInputElement>) =>{
                                setRoleName(e.target.value);
                                console.log(roleName);
                            }} />
                        </div>

                        <hr />

                        <h1 className='text-2xl font-semibold my-2'>Give Permissions</h1>

                        {
                            permissions.map((permissionCategory) => (
                                <div key={permissionCategory.topic} className='w-full my-4'>
                                    <h3 className="mb-2 font-semibold ">{permissionCategory.topic}</h3>
                                    <div className='grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 w-full gap-4 border border-medium p-4 rounded-lg'>
                                        {
                                            permissionCategory.actions.map((action) => (
                                                <PermissionCheckBox
                                                    key={action.id}
                                                    htmlForAndId={permissionCategory.topic}
                                                    labelName={action.action}
                                                    permissionId={action.id.toString()}
                                                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                                                        const permissionId = action.id
                                                        if (e.target.checked) {
                                                            setSelectedPermissions(prevPermissions => [...prevPermissions, permissionId]);
                                                        } else {
                                                            setSelectedPermissions(prevPermissions => prevPermissions.filter(id => id !== permissionId));
                                                        }
                                                    }}
                                                />
                                            ))
                                        }
                                    </div>
                                </div>
                            ))
                        }

                        <div className='flex w-full justify-end my-4'>
                            <button type="submit" className=" text-black box-border border border-transparent hover:bg-brand-strong focus:ring-4 focus:ring-brand-medium shadow-xs 
                        font-medium leading-5 rounded-base text-sm px-4 py-2.5 focus:outline-none mr-4">Submit</button>
                        </div>
                    </form>
                </>
            }
        </div>
    )
}

export default AddRole