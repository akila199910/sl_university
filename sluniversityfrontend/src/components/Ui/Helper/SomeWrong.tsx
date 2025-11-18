import Link from 'next/link'
import React from 'react'

const SomeWrong = ({url}:{url:string}) => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-amber-50 bg-opacity-75" aria-modal="true" role="dialog">
        {/* Modal content */}
        <div className="bg-white p-6 rounded-lg shadow-xl w-full max-w-sm m-4">
        
            <h1 className='text-center text-2xl mb-3'>Something went wrong !</h1>
            <div className="flex justify-center gap-4">
                <Link href={url}
                    
                    className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 disabled:bg-blue-300 flex items-center justify-center w-28"
                >
                    Back
                </Link>
            </div>
        </div>
    </div>
  )
}

export default SomeWrong