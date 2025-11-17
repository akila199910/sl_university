"use client";
import React from 'react';

interface PermissionCheckBoxProps {
    labelName: string;
    htmlForAndId: string;
    permissionId?: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    checked?: boolean;
}

const PermissionCheckBox = (props: PermissionCheckBoxProps) => {
    
    const inputId = `${props.labelName}-${props.htmlForAndId}-${props.permissionId}`;

    return (
        <div className="flex items-center ps-3">
            <input
                id={inputId}
                type="checkbox"
                value={props.permissionId}
                className="w-4 h-4 border-medium rounded-xs focus:ring-2 focus:ring-brand-soft"
                onChange={props.onChange}
                checked={props.checked}
            />
            <label
                className="w-full ms-2 text-sm font-medium "
            >
                {props.labelName}
            </label>
        </div>
    );
};

export default PermissionCheckBox;