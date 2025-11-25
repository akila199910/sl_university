import React, { Suspense } from 'react'
import RolesPageContent from './RolesPageContent'

const RolePage = () => {
  return (
    <Suspense fallback={<div className='bg-white shadow-md rounded-lg max-w-6xl mx-auto p-4'><p>Loading roles…</p></div>}>
      <RolesPageContent />
    </Suspense>
  )
}

export default RolePage