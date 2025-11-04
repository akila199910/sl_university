import React from 'react'

interface DataTableProps {
    columns?: any[];
    data?: any[];
    page: number;
    size: number;
    totalPages: number;
    totalElements: number;
}
const DataTable = ({ columns, data, page = 0, size = 0, totalPages = 0, totalElements = 0 }: DataTableProps) => {
    return (
        <>
            <div className='overflow-x-auto'>
                <table className='table-auto w-full border-collapse border border-gray-300'>
                    <thead>

                        <tr className='bg-gray-200'>
                            {columns && columns.length > 0 ? (
                                columns.map((col) => (
                                    <th key={col.accessor} className='border border-gray-300 px-4 py-2'>
                                        {col.header}
                                    </th>
                                ))
                            ) : (
                                <th className='border border-gray-300 px-4 py-2'>No Columns</th>
                            )}
                        </tr>
                    </thead>
                    <tbody>
                        {data && data.length > 0 ? (
                            data.map((u, index) => (
                                <tr key={u.id}>
                                    <td className='border border-gray-300 px-4 py-2'>{page * size + index + 1}</td>
                                    <td className='border border-gray-300 px-4 py-2'>
                                        <div className='flex items-center gap-2'>
                                            {u.profile && (u.profile as any).profileUrl ? (
                                                <div>A</div>
                                                // <img src={(u.profile as any).profileUrl} alt={u.name} className='w-8 h-8 rounded-full' />
                                            ) : (
                                                <div className='w-8 h-8 rounded-full bg-gray-300' />
                                            )}
                                            <span>{u.name}</span>
                                        </div>
                                    </td>
                                    <td className='border border-gray-300 px-4 py-2'>{u.email}</td>
                                    <td className='border border-gray-300 px-4 py-2'>{u.roles.map((r: any) => r.name)}</td>
                                    <td className='border border-gray-300 px-4 py-2'>{u.contactNumber}</td>
                                    <td className='border border-gray-300 px-4 py-2'>
                                        {u.status ? (
                                            <span className='text-green-700'>Active</span>
                                        ) : (
                                            <span className='text-gray-600'>Inactive</span>
                                        )}
                                    </td>
                                    <td className='border border-gray-300 px-4 py-2'>
                                        {u.viewUrl != null && (
                                            <button
                                                className='px-2 py-1 bg-green-500 text-white rounded mr-2 hover:bg-green-600'
                                                onClick={() => alert(`Edit user ${u.id}`)}
                                            >
                                                View
                                            </button>
                                        )}

                                        {u.editUrl != null && (
                                            <button
                                                className='px-2 py-1 bg-blue-500 text-white rounded mr-2 hover:bg-blue-600'
                                                onClick={() => alert(`Edit user ${u.id}`)}
                                            >
                                                Edit
                                            </button>
                                        )}

                                        {u.deleteUrl != null && (
                                            <button
                                                className='px-2 py-1 bg-red-500 text-white rounded hover:bg-red-600'
                                                onClick={() => alert(`Delete user ${u.id}`)}
                                            >
                                                Delete
                                            </button>
                                        )}


                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td className='border border-gray-300 px-4 py-2' colSpan={6}>
                                    No users found.
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            <div className='flex items-center justify-between mt-4'>
                {/* <div>
                            <button
                                onClick={() => gotoPage(page - 1)}
                                disabled={page <= 0}
                                className='px-3 py-2 mr-2 bg-white border border-gray-300 rounded disabled:opacity-50'
                            >Previous</button>

                            {renderPageButtons()}

                            <button
                                onClick={() => gotoPage(page + 1)}
                                disabled={page >= totalPages - 1}
                                className='px-3 py-2 ml-2 bg-white border border-gray-300 rounded disabled:opacity-50'
                            >Next</button>
                        </div> */}

                <div className='flex items-center gap-4'>
                    {/* <div className='text-sm text-gray-700'>
                                Page {page + 1} of {totalPages} — {totalElements} users
                            </div> */}

                    <label className='text-sm'>
                        {/* <select value={size} onChange={(e) => changeSize(Number(e.target.value))} className='border rounded px-2 py-1'>
                                    <option value={10}>10</option>
                                    <option value={20}>20</option>
                                    <option value={50}>50</option>
                                </select> */}
                    </label>
                </div>
            </div>
        </>
    )
}

export default DataTable