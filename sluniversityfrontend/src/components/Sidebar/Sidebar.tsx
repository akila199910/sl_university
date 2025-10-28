"use client"
import Link from 'next/link'
import React, { useState } from 'react'
import sidebarLinks from './sidebarLink'
import up from './icons/chevron-up.svg'
import Image from 'next/image'


const Sidebar = () => {

  const [openSublinks, setOpenSublinks] = useState<number[]>([]);

  const handleShow = (e: React.MouseEvent<HTMLDivElement>, linkId: number) => {
    e.preventDefault();
    setOpenSublinks(prev =>
      prev.includes(linkId)
        ? prev.filter(id => id !== linkId)
        : [...prev, linkId]
    );
  }
  return (
    <>
      {
        sidebarLinks.map((sidebarLink) => {

          if (sidebarLink.multy !== null && sidebarLink.multy === true) {
            return (
              <div key={sidebarLink.id} className='flex flex-col'>
                <div className='flex items-center cursor-pointer
                   rounded-md mb-2 hover:bg-gray-400 p-2 '>

                  <div className='flex items-center w-full justify-between' onClick={(e) => { handleShow(e, sidebarLink.id) }}>
                    <div className='flex gap-2'>
                      <Image src={sidebarLink.icon} alt={sidebarLink.name} className='w-5'/>
                      <div className='text-start'>{sidebarLink.name}</div>
                    </div>
                    <div className='flex'>
                      <Image src={up} alt='up' className='w-5 relative top-1'/>
                    </div>
                  </div>
                </div>
                <div className='bg-amber-50 mb-2 rounded-md'>
                  {
                    sidebarLink.subLinks && sidebarLink.subLinks.map((subLink) => (
                      <div key={subLink.id} className={`overflow-hidden transition-all duration-500 ease-in-out ${openSublinks.includes(sidebarLink.id)
                        ? 'max-h-40 opacity-100'
                        : 'max-h-0 opacity-0'
                        }`}>
                        <Link key={subLink.id} className='flex items-center gap-2 cursor-pointer
                        p-2 ml-4' href={subLink.link}>
                          <Image src={subLink.icon} alt={subLink.name} className='w-5'/>
                          <div className='text-start'>{subLink.name}</div>
                        </Link>
                      </div>

                    ))
                  }
                </div>

              </div>

            );
          } else {
            return (
              <Link key={sidebarLink.id} className='flex items-center gap-2 cursor-pointer
               mb-2 hover:bg-gray-400 p-2 rounded-md' href={sidebarLink.link}>
                <Image src={sidebarLink.icon} alt={sidebarLink.name} className='w-5'/>
                <div className='text-start'>{sidebarLink.name}</div>
              </Link>
            );
          }

        })}
    </>
  )
}

export default Sidebar