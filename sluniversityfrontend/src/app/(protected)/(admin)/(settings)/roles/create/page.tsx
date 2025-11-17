import PermissionCheckBox from '@/components/Ui/Form/PermissionCheckBox'
import TextFormInput from '@/components/Ui/Form/TextFormInput'
import React from 'react'

const AddRole = () => {
    return (
        <div className='bg-amber-100 m-2 p-2 rounded-2xl max-w-6xl mx-auto'>
            <h1 className='text-2xl font-semibold mb-4'>ADD NEW ROLE</h1>

            <form>
                <div className="grid gap-6 mb-6 md:grid-cols-2">
                    <TextFormInput htmlForAndId='role_name' labelNaame='Role Name' isRequired={true} placeHolder='Role Name' />
                </div>

                <hr />

                <h1 className='text-2xl font-semibold my-2'>Give Permissions</h1>

                <div className='w-full'>
                    <h3 className="mb-2 font-semibold ">Technology</h3>
                   
                    <div className='grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 w-full gap-4 border border-medium p-4 rounded-lg'>

                        <PermissionCheckBox htmlForAndId='P' labelName = "P" permissionId='1'/>
                        
                    </div>
                </div>

                <div className='flex w-full justify-end my-4'>
                    <button type="submit" className=" text-black box-border border border-transparent hover:bg-brand-strong focus:ring-4 focus:ring-brand-medium shadow-xs 
                        font-medium leading-5 rounded-base text-sm px-4 py-2.5 focus:outline-none mr-4">Submit</button>
                </div>
            </form>

        </div>
    )
}

export default AddRole