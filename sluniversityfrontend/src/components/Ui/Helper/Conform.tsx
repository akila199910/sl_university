import React from 'react'
import Image from 'next/image';
import loader from '../../../../public/loader-black.svg'

const Conform = ({ onConfirm, onCancel, isLoading, message }: { onConfirm: () => void, onCancel: () => void, isLoading: boolean , message:string}) => (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-amber-50 bg-opacity-75" aria-modal="true" role="dialog">
        {/* Modal content */}
        <div className="bg-white p-6 rounded-lg shadow-xl w-full max-w-sm m-4">
            <h2 className="text-lg font-semibold mb-4">Confirm Submission</h2>
            <p className="text-gray-600 mb-6 font-medium">{message}</p>
            <div className="flex justify-end gap-4">
                <button
                    type="button"
                    onClick={onCancel}
                    disabled={isLoading}
                    className="px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 disabled:opacity-50"
                >
                    Cancel
                </button>
                <button
                    type="button"
                    onClick={onConfirm}
                    disabled={isLoading}
                    className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 disabled:bg-blue-300 flex items-center justify-center w-28"
                >
                    {isLoading ? <Image src={loader} alt='loader'/> : "Confirm"}
                </button>
            </div>
        </div>
    </div>
);

export default Conform