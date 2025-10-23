import Link from 'next/link'
import React from 'react'

const Sidebar = () => {
  return (
        <>
            <Link className='flex items-center gap-2 cursor-pointer
             bg-blue-300 rounded-md mb-2 hover:bg-blue-800' href={"/"}>
                <div className='p-2'>IC</div>
                <div className='p-2 text-start'>Name</div>
            </Link>
        </>
  )
}

export default Sidebar