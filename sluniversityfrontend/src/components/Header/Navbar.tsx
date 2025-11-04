"use client"
import React, { useState } from 'react'
import { manuTemItems } from './menuItem'
import Link from 'next/link'

const Navbar = ({ onToggleSidebar }: { onToggleSidebar: () => void }) => {

  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState<boolean>(false);
  return (
    <div className='flex justify-between items-center px-4 sm:px-6 md:px-8 lg:px-12 py-4 shadow-md z-99 '>

      {/* navbar logo and app name */}
      <div className='flex items-center gap-4'>
        <div className='' onClick={onToggleSidebar}>APP Logo</div>
        <div className=' hidden sm:flex font-bold text-2xl'>App Name</div>
      </div>

      {/* navbar menu items */}
      {/* <div className=" hidden sm:flex gap-3">
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
      </div> */}

      {/* user profile icon and mobile menu*/}

      <div className='flex flex-col'>
        <div className='rounded-full bg-amber-500 p-2 uppercase cursor-pointer' onClick={()=>setIsMobileMenuOpen(!isMobileMenuOpen)}>AU</div>
        <div className={` ${isMobileMenuOpen ? 'flex' : 'hidden'} absolute top-16 right-4 sm:right-16 bg-white shadow-lg rounded-md p-2`}>
          <ul>
            {manuTemItems.map((item, index) => (
              <li key={index} className="px-4 py-2 hover:bg-gray-100 rounded-md">
                <Link href={item.link} onClick={()=>setIsMobileMenuOpen(!isMobileMenuOpen)}>{item.name}</Link>
              </li>
            ))}
          </ul>
        </div>
      </div>

    </div>
  )
}

export default Navbar