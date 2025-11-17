import React from 'react'

interface PermissionCheckBoxProps {
    labelName: string;
    htmlForAndId: string;
    permissionId?: string;
}

const PermissionCheckBox = (PermissionCheckBox:PermissionCheckBoxProps) => {
  return (
    <div className="flex items-center ps-3">
      <input id={PermissionCheckBox.labelName+PermissionCheckBox.htmlForAndId} type="checkbox" value={PermissionCheckBox.permissionId} className="w-4 h-4 border-medium rounded-xs focus:ring-2 focus:ring-brand-soft" />
      <label  className="w-full ms-2 text-sm font-medium ">{PermissionCheckBox.labelName}</label>
    </div>
  )
}

export default PermissionCheckBox