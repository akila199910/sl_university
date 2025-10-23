"use client"
import Link from 'next/link'
import React, { MouseEventHandler, useState } from 'react'
import sidebarLinks from './sidebarLink'

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
                <div className='flex items-center gap-2 cursor-pointer
               bg-blue-300 rounded-md mb-2 hover:bg-blue-800 p-2'>
                  <div className='text-start'>{sidebarLink.icon}</div>
                  <div className='text-start'>{sidebarLink.name}</div>
                  <div className='text-start' onClick={(e) => { handleShow(e, sidebarLink.id) }}>Icon</div>
                </div>
                <div className='bg-red-900 mb-2 rounded-md'>
                  {
                    sidebarLink.subLinks && sidebarLink.subLinks.map((subLink) => (
                      <div key={subLink.id} className={` ${openSublinks.includes(sidebarLink.id) ? '' : 'hidden'} mb-`}>
                        <Link key={subLink.id} className='flex items-center gap-2 cursor-pointer
                        p-2 ml-4' href={subLink.link}>
                          <div className='text-start'>{subLink.icon}</div>
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
               bg-blue-300 rounded-md mb-2 hover:bg-blue-800 p-2' href={sidebarLink.link}>
                <div className='text-start'>{sidebarLink.icon}</div>
                <div className='text-start'>{sidebarLink.name}</div>
              </Link>
            );
          }

        })}
    </>
  )
}

export default Sidebar