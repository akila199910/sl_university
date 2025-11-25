"use client";

const AddRecord = ({message}:{message:string}) => {
  return (
    <div className='py-2 text-green-800 bg-green-300 w-auto rounded-2xl inline-flex px-3 mb-2 font-medium'>
         <h1 className='text-center'>
           {message}
         </h1>
    </div>
  )
}

export default AddRecord