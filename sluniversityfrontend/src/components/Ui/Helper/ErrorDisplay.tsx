"use client";
import React from 'react'
import { Interface } from 'readline';

interface ErrorDisplayProp {
    message: any
}

const ErrorDisplay = ({ message }:ErrorDisplayProp) => (
    <div className='text-red-700 text-center p-6 bg-red-100 rounded-lg shadow-inner'>
        <h3 className="font-bold text-lg">Oops, something went wrong.</h3>
        <p>Error: {message}</p>
    </div>
);


export default ErrorDisplay