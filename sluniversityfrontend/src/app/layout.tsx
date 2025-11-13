"use client";
import { AuthProvider } from "@/app/context/AuthContext";
import Navbar from "@/components/Header/Navbar";
import "./globals.css";
import Sidebar from "@/components/Sidebar/Sidebar";
import { useState } from "react";

export default function RootLayout({ children }: { children: React.ReactNode }) {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleToggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  return (
    <html lang="en">
      <body className="bg-amber-50">
        <div className="flex flex-col min-h-screen w-full">

          {/* Navbar */}
          <div className="w-full fixed z-50">
            <Navbar onToggleSidebar={handleToggleSidebar} />
          </div>

          <div className="flex mt-20 h-[calc(100vh-5rem)] relative">
            {/* Sidebar */}
            <aside
              className={`
                shadow-md bg-amber-50 fixed top-20 left-0 h-[calc(100vh-5rem)]
                overflow-y-auto transition-all duration-300 ease-in-out
                ${sidebarOpen ? "w-56 z-50" : "w-16 z-50"}
                md:w-56
              `}
            >
              <Sidebar isExpanded={sidebarOpen} />
            </aside>

            {/* Overlay on mobile */}
            {sidebarOpen && (
              <div
                className="fixed  md:hidden z-40"
                onClick={() => setSidebarOpen(false)}
              ></div>
            )}

            {/* Main content */}
            <main
              className={`
                flex-1 transition-all duration-300 ease-in-out
                ${sidebarOpen ? "ml-56" : "ml-16"}
                md:ml-56
                px-4 py-6 overflow-y-auto
              `}
            >
              <AuthProvider>
                {children}
              </AuthProvider>
            </main>
          </div>
        </div>
      </body>
    </html>
  );
}
