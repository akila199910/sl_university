import React, { Suspense } from 'react'
import UsersPageContent from './UsersPageContent'

const UsersPage = () => {
  return (
    <Suspense fallback={<div className='bg-white shadow-md rounded-lg max-w-6xl mx-auto p-4'><p>Loading users…</p></div>}>
      <UsersPageContent />
    </Suspense>
  )
}

export default UsersPage