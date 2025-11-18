import React from 'react'
import LoaderBlaclIcon from '../../../../public/loader-black.svg'
import LoaderWhiteIcon from '../../../../public/loader-white.svg'
import Image from 'next/image';


const Loader = ({ message = "Loading..." }) => (
    <div className="flex flex-col items-center justify-center p-20 min-h-[300px] bg-white rounded-lg shadow-md">
        <Image src={LoaderBlaclIcon} alt='loader' />
        <span className="mt-4 text-lg font-medium text-gray-700">{message}</span>
    </div>
);

export default Loader