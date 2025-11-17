import React from 'react'

interface TextFormInputProps {
    labelNaame: string;
    placeHolder: string;
    htmlForAndId: string;
    isRequired?: boolean;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    value?:string
}

const TextFormInput = (TextFormInputProps:TextFormInputProps) => {
    return (
        <div>
            <label htmlFor={TextFormInputProps.htmlForAndId} className="block mb-2.5 text-sm font-medium">{TextFormInputProps.labelNaame}</label>
            <input type="text" id={TextFormInputProps.htmlForAndId} 
            className=" border rounded-2xl text-sm rounded-base focus:ring-brand 
            focus:border-brand block w-full px-3 py-2.5 shadow-xs placeholder:text-body" 
            placeholder={TextFormInputProps.placeHolder} required={TextFormInputProps.isRequired}
            onChange={TextFormInputProps.onChange}
            value={TextFormInputProps.value}
             />
        </div>
    )
}

export default TextFormInput