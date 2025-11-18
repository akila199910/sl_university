import React, { MouseEvent } from 'react'

interface RadioButtonProps {
  isCheck: boolean;
  onClick: (e: MouseEvent<HTMLInputElement>) => void;
}

export const RadioButton = ({ isCheck, onClick }: RadioButtonProps) => {
  return (
    <div className="flex items-center">
      <input 
        type="checkbox" 
        checked={isCheck} 
        readOnly 
        className={`
          w-4 h-4 rounded-full appearance-none cursor-pointer
          border border-gray-300 transition-all duration-200
          checked:bg-amber-500 checked:border-amber-500 
          focus:ring-2 focus:outline-none focus:ring-amber-200
        `}
        onClick={onClick}
      />
      <label 
        onClick={(e: any) => onClick(e)} 
        className="select-none ms-2 text-sm font-medium text-gray-700 cursor-pointer"
      >
        {isCheck ? 'Active' : 'Inactive'}
      </label>
    </div>
  )
}