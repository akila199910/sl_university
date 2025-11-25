import React from 'react'

interface ConformationModelProps {
   
    isOpen: boolean;
    onClose: () => void;
    onConfirm: () => void;
    title: string;
    message: string;
    isDestructive?: boolean;
}

const SuccessModel: React.FC<ConformationModelProps> = ({
    isOpen,
    onClose,
    onConfirm,
    title,
    message,
    isDestructive = false,
})=> {
  if (!isOpen) {
        return null;
    }
    const confirmButtonClasses = isDestructive
        ? 'bg-red-600 hover:bg-red-700'
        : 'bg-green-600 hover:bg-green-700';

    return (
        <div 
            className='fixed inset-0  bg-opacity-10 w-screen h-screen flex justify-center items-center z-10 transition-opacity duration-500'
            onClick={onClose}
        >
            {/* Modal Content Box */}
            <div 
                className='bg-gray-100 rounded-xl shadow-2xl w-full max-w-sm m-4 transform transition-all'
                onClick={(e) => e.stopPropagation()} // Prevents closing when clicking on the modal content
            >
                {/* Header */}
                <div className="px-6 py-4 border-b border-gray-200">
                    <h3 className="text-xl font-semibold text-gray-900">{title}</h3>
                </div>

                {/* Body */}
                <div className="p-6">
                    <p className="text-sm text-gray-700">{message}</p>
                </div>

                {/* Footer (Actions) */}
                <div className="px-6 py-4 bg-gray-100 flex justify-center gap-3 rounded-b-xl">
                    <button
                        type="button"
                        className={`px-4 py-2 text-sm font-medium rounded-lg text-white transition ${confirmButtonClasses}`}
                        onClick={onConfirm}
                    >
                        Yes
                    </button>
                </div>
            </div>
        </div>
    );
}

export default SuccessModel