import Navbar from "@/components/Header/Navbar";
import "./globals.css";
import Sidebar from "@/components/Sidebar/Sidebar";

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body>
        <div className="flex flex-col min-h-screen w-full">

          <div className="w-full fixed z-999"><Navbar/></div>

          {/* main section */}
          <div className="h-full flex flex-grow relative top-18 ">

            {/* sidebar section */}
            <aside className=" bg-amber-500 shadow-md w-44 rounded-md fixed h-full px-4 py-6 overflow-y-auto">
              <Sidebar/>
            </aside>

            <main className="flex-1 ml-46  bg-blue-200 shadow-md rounded-md px-4 py-6">
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
