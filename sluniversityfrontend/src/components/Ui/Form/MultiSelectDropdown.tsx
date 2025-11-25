"use client";
import React, { useState, useRef, useEffect } from 'react';

// Define the shape of a single option
export type Option = {
    id: number | string;
    name: string;
};

interface MultiSelectProps {
    labelName: string;
    options: Option[];             // All available options
    selectedIds: (number | string)[]; // Array of currently selected IDs
    onChange: (selectedIds: (number | string)[]) => void; // Callback to update parent state
    placeholder?: string;
    htmlForAndId?: string;
}

const MultiSelectDropdown = ({ 
    labelName, 
    options, 
    selectedIds, 
    onChange, 
    placeholder = "Select options...", 
    htmlForAndId 
}: MultiSelectProps) => {
    
    const [isOpen, setIsOpen] = useState(false);
    const dropdownRef = useRef<HTMLDivElement>(null);

    const toggleDropdown = () => setIsOpen(!isOpen);

    // Handle selecting/deselecting an item
    const handleSelect = (id: number | string) => {
        if (selectedIds.includes(id)) {
            onChange(selectedIds.filter((item) => item !== id)); // Remove if already selected
        } else {
            onChange([...selectedIds, id]); // Add if not selected
        }
    };

    // Close dropdown if clicking outside of it
    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
                setIsOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    return (
        <div className="flex flex-col relative" ref={dropdownRef}>
            {/* Label - Same style as your TextFormInput */}
            <label htmlFor={htmlForAndId} className="block mb-2.5 text-sm font-medium">
                {labelName}
            </label>

            {/* The Input-like Container */}
            <div 
                className={`
                    border rounded-2xl text-sm rounded-base shadow-xs bg-white
                    block w-full px-3 py-2.5 min-h-[46px] cursor-pointer flex items-center justify-between
                    ${isOpen ? 'ring-2 ring-brand border-brand' : 'border-gray-300'} 
                `}
                onClick={toggleDropdown}
            >
                <div className="flex flex-wrap gap-2">
                    {selectedIds.length > 0 ? (
                        selectedIds.map((id) => {
                            const option = options.find((o) => o.id === id);
                            return (
                                <span key={id} className="bg-amber-100 text-amber-800 text-xs font-semibold px-2.5 py-0.5 rounded-full flex items-center gap-1">
                                    {option?.name}
                                    <button 
                                        type="button"
                                        onClick={(e) => { e.stopPropagation(); handleSelect(id); }}
                                        className="hover:text-amber-900 font-bold"
                                    >
                                        ×
                                    </button>
                                </span>
                            );
                        })
                    ) : (
                        <span className="text-gray-400">{placeholder}</span>
                    )}
                </div>

                {/* Arrow Icon */}
                <svg className={`w-4 h-4 text-gray-500 transition-transform ${isOpen ? 'rotate-180' : ''}`} fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 9l-7 7-7-7" />
                </svg>
            </div>

            {/* Dropdown Menu */}
            {isOpen && (
                <div className="absolute z-10 top-[80px] w-full bg-white border border-gray-200 rounded-xl shadow-lg max-h-60 overflow-y-auto">
                    {options.length > 0 ? (
                        options.map((option) => (
                            <div 
                                key={option.id}
                                onClick={() => handleSelect(option.id)}
                                className={`
                                    px-4 py-2 cursor-pointer text-sm flex items-center gap-2 hover:bg-gray-50
                                    ${selectedIds.includes(option.id) ? 'bg-amber-50 text-amber-900' : 'text-gray-700'}
                                `}
                            >
                                <input 
                                    type="checkbox" 
                                    checked={selectedIds.includes(option.id)} 
                                    readOnly 
                                    className="w-4 h-4 rounded text-brand focus:ring-brand border-gray-300"
                                />
                                {option.name}
                            </div>
                        ))
                    ) : (
                        <div className="px-4 py-2 text-sm text-gray-500">No options available</div>
                    )}
                </div>
            )}
        </div>
    );
};

export default MultiSelectDropdown;