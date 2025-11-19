"use client"
import api from '@/app/lib/api';
import MultiSelectDropdown from '@/components/Ui/Form/MultiSelectDropdown';
import DropDown from '@/components/Ui/Form/MultiSelectDropdown';
import TextFormInput from '@/components/Ui/Form/TextFormInput'
import Conform from '@/components/Ui/Helper/Conform';
import SomeWrong from '@/components/Ui/Helper/SomeWrong';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import React, { useEffect, useState } from 'react'

type Role = {
  id: number,
  name: string
}
type Validation = {
  firstName: string,
  lastName: string,
  email: string,
  contactNumber: string
}
const UserCreate = () => {

  const router = useRouter();

  const [roles, setRoles] = useState<Role[]>([])
  const [loading, setLoading] = useState<boolean>(true);
  const [validationErrors, setValidationErrors] = useState<Validation | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [showSomethinWrong, setShowSomethinWrong] = useState(false);
  const [selectedRoles, setSelectedRoles] = useState<(number | string)[]>([]);

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    contactNumber: ''
  });

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      const { id, value } = e.target;
      setFormData(prev => ({
          ...prev,
          [id]: value
      }));
  };
  
  useEffect(
    () => {
      const fetchRoles = async () => {

        setLoading(true);
        setRoles([]);
        try {
          const response = await api.get("system_users/create");
          const data = response.data
          setRoles(data.data.roles);
          setLoading(false);

        } catch (error: any) {

          if (error.status == 500) {
            setShowSomethinWrong(true)
          }
          setLoading(false);
        }
        finally {
          setLoading(false);
        }

      }
      fetchRoles()
    }, [])

  const handleSubmit = async (e: React.FormEvent) => {

    e.preventDefault();
    setValidationErrors(null);
    setShowConfirmModal(true);
  }

  const handleConfirmSubmit = async () => {
    setIsSubmitting(true);
    setValidationErrors(null);
    try {
      const payload = {
                firstName: formData.firstName,
                lastName: formData.lastName,
                email: formData.email,
                contactNumber: formData.contactNumber,
                roles: selectedRoles
            };
      const response = await api.post("system_users",payload);

      router.push(`/users?success=true`);

    } catch (err: any) {

      if (err.status == 500) {
        setShowSomethinWrong(true)

      } else {
        setValidationErrors(err.response.data.errors);
        setIsSubmitting(false);
        setShowConfirmModal(false);
      }
    }
  };

  return (
    <div className='m-2 p-2 rounded-2xl max-w-6xl mx-auto relative'>

      {loading && <p>Loading ...</p>}

      {!loading &&
        <div className='space-y-2 bg-white shadow-md rounded-lg p-6'>

          <div className='flex justify-between items-center'>
            <h1 className='text-2xl font-semibold mb-4'>CREATE ROLE</h1>
            <div className="flex justify-end mb-2">
              <Link href="/users" className="bg-blue-500 hover:bg-blue-600 text-white 
                            py-1 sm:py-2 px-2 sm:px-4 rounded-md transition cursor-pointer"

              >
                Back
              </Link>
            </div>
          </div>

          <form onSubmit={handleSubmit} className=''>

            <div className="grid gap-6 mb-6 md:grid-cols-2  mx-auto">
              <div className='flex flex-col'>
                <TextFormInput htmlForAndId='firstName' labelNaame='First Name'
                  isRequired={false} placeHolder='First Name'
                  value={formData.firstName} 
                  onChange={handleInputChange}  />

                {validationErrors?.firstName && (<span className='text-xs text-red-500 mt-1'>{validationErrors?.firstName}</span>)}
              </div>
              <div className='flex flex-col'>
                <TextFormInput htmlForAndId='lastName' labelNaame='Last Name'
                  isRequired={false} placeHolder='Last Name'
                  value={formData.lastName} 
                  onChange={handleInputChange}/>

                {validationErrors?.lastName && (<span className='text-xs text-red-500 mt-1'>{validationErrors?.lastName}</span>)}
              </div>
              <div className='flex flex-col'>
                <TextFormInput htmlForAndId='email' labelNaame='Email'
                  isRequired={false} placeHolder='email@gmail.com'
                  value={formData.email} 
                  onChange={handleInputChange} />

                {validationErrors?.email && (<span className='text-xs text-red-500 mt-1'>{validationErrors?.email}</span>)}
              </div>
              <div className='flex flex-col'>
                <TextFormInput htmlForAndId='contactNumber' labelNaame='Contact Number'
                  isRequired={false} placeHolder='Contact Number'
                  value={formData.contactNumber} 
                  onChange={handleInputChange} />

                {validationErrors?.contactNumber && (<span className='text-xs text-red-500 mt-1'>{validationErrors?.contactNumber}</span>)}
              </div>

              <div className='flex flex-col'>
                <MultiSelectDropdown
                  htmlForAndId="Roles"
                  labelName="Roles"
                  options={roles}
                  selectedIds={selectedRoles}
                  onChange={(newSelection) => setSelectedRoles(newSelection)}
                  placeholder="Select Roles"
                />
                {/* Error handling example */}
                {/* {validationErrors?.permissions && (
                                    <span className='text-xs text-red-500 mt-1'>{validationErrors.permissions}</span>
                                )} */}
              </div>
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
          message="Are you sure you want to create this role?"
        />
      )}

      {showSomethinWrong && (
        <SomeWrong url="/users" />
      )}

    </div>
  )
}

export default UserCreate