"use client";

import React from 'react'
import { useAuth } from '@/app/context/AuthContext'


const page = () => {

  const {user} = useAuth();
  

  return (
   <div className='m-2 p-2 bg-amber-300 rounded-2xl'>

    <h1 className='text-2xl font-bold'>Dashboard Home Page</h1>
    <div>Welcomee { user?.name}</div>
    
   </div>
  )
}

export default page