"use client"
import React from 'react'
import { manuTemItems } from './menuItem'
import Link from 'next/link'

const Navbar = () => {
  return (
    <div className='flex justify-between items-center px-4 sm:px-6 md:px-8 lg:px-12 py-4 bg-amber-50 shadow-md '>

      {/* navbar logo and app name */}
        <div className='flex items-center gap-4'>
          <div className=''>APP Logo</div>
          <div className='font-bold text-2xl'>App Name</div>
        </div>

        {/* navbar menu items */}
        <div className="flex gap-3">
          {manuTemItems.map((item, index) => {
            return (
              <Link
                key={index}
                href={item.link}
                className="text-gray-700 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
              >
                {item.name}
              </Link>
            );
          })}
        </div>
        {/* user profile icon */}
          <div className='rounded-full bg-amber-500 p-2 uppercase cursor-pointer'>AU</div>
      
    </div>
  )
}

export default Navbar