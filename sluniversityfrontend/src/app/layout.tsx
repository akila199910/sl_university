import Navbar from "@/components/Header/Navbar";
import "./globals.css";
import Sidebar from "@/components/Sidebar/Sidebar";

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body className="bg-amber-50">
        <div className="flex flex-col min-h-screen w-full">

          <div className="w-full fixed z-999"><Navbar /></div>

          <div className="flex mt-20 h-[calc(100vh-5rem)] ">
            {/* Sidebar */}
            <aside className="shadow-md w-56  fixed top-20 left-0 h-[calc(100vh-5rem)] px-4 py-6 overflow-y-auto">
              <Sidebar />
            </aside>

            {/* Main content */}
            <main className=" flex-1 ml-56 shadow-md  px-4 py-6 overflow-y-auto">
              {children}
            </main>
          </div>

          {/* {children} */}
          {/* <div className="">Footer</div> */}
        </div>
      </body>
    </html>
  );
}
