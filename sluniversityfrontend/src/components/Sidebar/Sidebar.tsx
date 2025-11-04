"use client";
import Link from "next/link";
import React, { useState } from "react";
import sidebarLinks from "./sidebarLink";
import up from "./icons/chevron-up.svg";
import Image from "next/image";

const Sidebar = ({ isExpanded }: { isExpanded: boolean }) => {
  const [openSublinks, setOpenSublinks] = useState<number[]>([]);
  console.log("Sidebar isExpanded:", isExpanded);

  const handleShow = (e: React.MouseEvent<HTMLDivElement>, linkId: number) => {
    e.preventDefault();
    setOpenSublinks((prev) =>
      prev.includes(linkId)
        ? prev.filter((id) => id !== linkId)
        : [...prev, linkId]
    );
  };

  return (
    <>
      {sidebarLinks.map((sidebarLink) => {
        if (sidebarLink.multy) {
          return (
            <div key={sidebarLink.id} className="flex flex-col">
              <div
                className="flex items-center cursor-pointer rounded-md mb-2 hover:bg-gray-400 p-2"
                onClick={(e) => handleShow(e, sidebarLink.id)}
              >
                <div className="flex items-center w-full justify-between">
                  <div className="flex gap-2 items-center">
                    <Image src={sidebarLink.icon} alt={sidebarLink.name} className="w-5" />
                    <div className={`${!isExpanded && 'hidden sm:flex'}`}>
                      {sidebarLink.name}
                    </div>

                  </div>
                  <Image src={up} alt="up" className={`${!isExpanded && 'hidden  relative top-1 sm:flex'} w-5`} />
                </div>
              </div>

              {sidebarLink.subLinks && (
                <div
                  className={`bg-amber-50 mb-2 rounded-md transition-all duration-500 ease-in-out ${openSublinks.includes(sidebarLink.id)
                      ? "max-h-40 opacity-100"
                      : "max-h-0 opacity-0"
                    }`}
                >
                  {sidebarLink.subLinks.map((subLink) => (
                    <Link
                      key={subLink.id}
                      href={subLink.link}
                      className="flex items-center gap-2 cursor-pointer p-2 ml-4"
                    >
                      <Image src={subLink.icon} alt={subLink.name} className="w-5" />
                      <div className={`${!isExpanded && 'hidden sm:flex'}`}>
                        {subLink.name}
                      </div>
                    </Link>
                  ))}
                </div>
              )}
            </div>
          );
        } else {
          return (
            <Link
              key={sidebarLink.id}
              href={sidebarLink.link}
              className="flex items-center gap-2 cursor-pointer mb-2 hover:bg-gray-400 p-2 rounded-md"
            >
              <Image src={sidebarLink.icon} alt={sidebarLink.name} className="w-5" />
              <div className={`${!isExpanded && 'hidden sm:flex'}`}>
                {sidebarLink.name}
              </div>
            </Link>
          );
        }
      })}
    </>
  );
};

export default Sidebar;
