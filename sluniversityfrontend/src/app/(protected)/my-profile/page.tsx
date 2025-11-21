"use client"
import api from '@/app/lib/api';
import React, { useEffect, useState } from 'react'
import coverImage from '../../images/cover.png'
import DummyImage from '../../images/dammyUser.png'
import ConformationModel from '@/components/Models/ConformationModel';

interface User {
    id: number | undefined;
    name: string;
    email: string;
    profileImageUrl?: string;
    coverImageUrl?: string;
    role: any;
    firstName: string,
    lastName: string,
    contactNumber: string
}

const MyProfile = () => {

    const [user, setUser] = useState<User | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [isSaving, setIsSaving] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);

    useEffect(() => {
        const checkUserStatus = async () => {
            try {

                const response: any = await api.get('/my-profile');
                const data = response.data.data;

                const profileData: User = {
                    id: data.id,
                    name: data.name,
                    email: data.email,
                    role: data.roles,
                    profileImageUrl: data.profileImageUrl,
                    coverImageUrl: data.coverImageUrl,
                    firstName: data.firstName,
                    lastName: data.lastName,
                    contactNumber: data.contactNumber
                };
                setUser(profileData);
            } catch (error: any) {
                console.error("Profile fetch error:", error);
                setError("Failed to load profile data.");
                setUser(null);
            } finally {
                setIsLoading(false);
            }
        };

        checkUserStatus();
    }, []);


    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (!user) return;

        setUser({
            ...user,
            [e.target.name]: e.target.value,
        } as User);
    };

    const handleConfirmAction = async () => {
          setIsModalOpen(false);
          if (!user || isSaving) return;

        try{

            setIsSaving(true);

            const response = await api.post("/my-profile",user)

            setIsSaving(false);

        }catch{

        }
      };


    if (isLoading) {
        return (
            <div className='max-w-6xl mx-auto py-20 text-center'>
                <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-indigo-600 inline-block"></div>
                <p className='mt-3 text-gray-600'>Loading user profile...</p>
            </div>
        );
    }

    if (error || !user) {
        return (
            <div className='max-w-6xl mx-auto py-20 text-center text-red-600 bg-red-50 rounded-lg'>
                <p className='font-semibold'>Error: {error || "User data could not be fetched."}</p>
                <p className='text-sm mt-1'>Please check your API configuration or network connection.</p>
            </div>
        );
    }

    return (
        <>
            <div className='bg-white shadow-2xl rounded-xl max-w-6xl mx-auto'>
                <form>

                    <div className='relative h-48 sm:h-64 rounded-t-xl overflow-hidden'>

                        <img
                            src={user.coverImageUrl && user.coverImageUrl !== "cover.png" ? user.coverImageUrl : coverImage.src}
                            alt='Cover Photo'
                            className='object-cover w-full h-full absolute inset-0'
                        />

                        {/* Cover Image Upload Button (Top Right) */}
                        <div className="absolute top-4 right-4 z-10">
                            <input type="file" id="file-cover" className="hidden" />
                            <label
                                htmlFor="file-cover"
                                className="
                                cursor-pointer flex items-center gap-1
                                bg-black bg-opacity-40 hover:bg-opacity-60 
                                text-white font-semibold text-xs sm:text-sm
                                py-1.5 px-3 rounded-full 
                                shadow transition duration-150 ease-in-out
                            "
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                                    <path fillRule="evenodd" d="M4 5a2 2 0 00-2 2v8a2 2 0 002 2h12a2 2 0 002-2v-8a2 2 0 00-2-2h-1.586a1 1 0 01-.707-.293l-1.586-1.586A.997.997 0 0010 3h-4a1 1 0 00-.707.293L4.707 5H4zm3 8a3 3 0 106 0 3 3 0 00-6 0z" clipRule="evenodd" />
                                </svg>
                                <span className="hidden sm:inline">Change Cover</span>
                            </label>
                        </div>
                    </div>

                    {/* 2. Profile Details Section (Overlapping) */}
                    <div className='px-6 pt-2 pb-8'>

                        {/* Profile Picture and Name Row */}
                        <div className='-mt-20 flex flex-col sm:flex-row justify-between items-start sm:items-end'>

                            {/* Profile Picture and Upload Button */}
                            <div className='relative w-36 h-36 sm:w-44 sm:h-44 rounded-full border-4 border-white shadow-xl group'>
                                <img
                                    src={user.profileImageUrl && user.profileImageUrl !== "profile.png" ? user.profileImageUrl : DummyImage.src}
                                    alt='Profile Picture'
                                    className='object-cover w-full h-full rounded-full'
                                />
                                <input type="file" id="file-profile" className="hidden" />
                                <label
                                    htmlFor="file-profile"
                                    className="
                                    absolute inset-0 flex items-center justify-center rounded-full 
                                    bg-black bg-opacity-0 group-hover:bg-opacity-40 transition duration-300
                                    text-white opacity-0 group-hover:opacity-100 cursor-pointer
                                "
                                    title="Change Profile Picture"
                                >
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8" viewBox="0 0 20 20" fill="currentColor">
                                        <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zm-3.5 6.707l-2.77 2.77-.707-.707 2.77-2.77 1.707 1.707z" />
                                        <path fillRule="evenodd" d="M11 6a5 5 0 11-10 0 5 5 0 0110 0zM17 6a5 5 0 11-10 0 5 5 0 0110 0z" clipRule="evenodd" />
                                    </svg>
                                </label>
                            </div>

                            {/* Name and Role */}
                            <div className='mt-2 sm:mt-0 sm:ml-6 text-left w-full'>
                                <h1 className="text-3xl font-extrabold text-gray-900 leading-tight">
                                    {user.firstName} {user.lastName}
                                </h1>
                            </div>
                        </div>

                        {/* 3. Form Grid for Editable Details */}
                        <div className='mt-10 pt-4 border-t border-gray-200'>
                            <h2 className='text-xl font-semibold text-gray-800 mb-4'>Personal Information</h2>

                            <div className='grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4 max-w-4xl'>

                                {/* First Name */}
                                {renderInput('firstName', 'First Name', 'text', user.firstName ?? '', handleChange)}

                                {/* Last Name */}
                                {renderInput('lastName', 'Last Name', 'text', user.lastName ?? '', handleChange)}

                                {/* Email */}
                                {renderInput('email', 'Email Address (Read Only)', 'email', user.email ?? '', handleChange, true)}

                                {/* Contact Number */}
                                {renderInput('contactNumber', 'Contact Number', 'tel', user.contactNumber ?? '', handleChange)}

                            </div>
                        </div>
                    </div>

                    <div className='bg-gray-50 px-6 py-4 flex justify-end rounded-b-xl'>
                        <button
                            type="button"
                            onClick={e=>setIsModalOpen(!isModalOpen)}
                            disabled={isSaving}
                            className={`
                            px-6 py-2 rounded-lg text-white font-semibold shadow-md 
                            transition duration-150 ease-in-out
                            ${isSaving ? 'bg-indigo-400 cursor-not-allowed' : 'bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-4 focus:ring-indigo-500 focus:ring-opacity-50'}
                        `}
                        >
                            {isSaving ? 'Saving...' : 'Save Changes'}
                        </button>
                    </div>
                </form>
            </div>
            <ConformationModel 
                    isOpen={isModalOpen} 
                    onClose={() => setIsModalOpen(false)} 
                    onConfirm={handleConfirmAction}
                    title="Are You Sure?"
                    message="This is a test message to ensure the modal renders correctly over the whole page."
                />
        </>

    );
};

const renderInput = (
    name: string,
    label: string,
    type: string,
    value: string,
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void,
    readOnly: boolean = false
) => (
    <div className='flex flex-col gap-y-1'>
        <label
            htmlFor={name}
            className={`text-sm font-medium ${readOnly ? 'text-gray-500' : 'text-gray-700'}`}
        >
            {label}
        </label>
        <input
            type={type}
            id={name}
            name={name}
            value={value}
            onChange={onChange}
            readOnly={readOnly}
            className={`
                block 
                w-full 
                px-3 py-2 
                border border-gray-300 
                rounded-lg 
                shadow-sm 
                sm:text-sm
                transition duration-150
                ${readOnly
                    ? 'bg-gray-100 text-gray-600 cursor-not-allowed border-dashed'
                    : 'bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500'
                }
            `}
        />
    </div>
);

export default MyProfile