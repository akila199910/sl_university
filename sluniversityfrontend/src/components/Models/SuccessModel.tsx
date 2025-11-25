import React from 'react'

interface ConformationModelProps {
   
    isOpen: boolean;
    onConfirm: () => void;
    title: string;
    message: string;
    isDestructive?: boolean;
}

const SuccessModel: React.FC<ConformationModelProps> = ({
    isOpen,
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
        >
            {/* Modal Content Box */}
            <div 
                className='bg-gray-100 rounded-xl shadow-2xl w-full max-w-sm m-4 transform transition-all'
                onClick={(e) => e.stopPropagation()} // Prevents closing when clicking on the modal content
            >
                {/* Header */}
                <h3
                    className={`text-xl font-semibold ${!isDestructive ? "text-green-900" : "text-red-700"} px-5 py-2`}
                    >
                    {title}
                </h3>


                {/* Body */}
                <div className="p-2 mt-2">
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