import TextFormInput from '@/components/Ui/Form/TextFormInput'
import React from 'react'

const UserCreate = () => {
  return (
    <div className='bg-amber-100 m-2 p-2 rounded-2xl max-w-6xl mx-auto'>
      <h1 className='text-2xl font-semibold mb-4'>Creat New User</h1>


      <>

        <form>
          <div className="grid gap-6 mb-6 md:grid-cols-2">
            <TextFormInput htmlForAndId='first_name' labelNaame='First Name'
            onChange={(e:React.ChangeEvent<HTMLInputElement>) =>{
                                            // setRoleName(e.target.value);
                                            // console.log(roleName);
                                        }}
             isRequired={true} placeHolder='First Name'/>

            <TextFormInput htmlForAndId='last_name' labelNaame='Last Name'
             isRequired={true} placeHolder='Last Name'
             onChange={(e:React.ChangeEvent<HTMLInputElement>) =>{
                                            //  setRoleName(e.target.value);
                                            //  console.log(roleName);
                                         }}/>
          </div>
          {/* <div className="mb-6">
            <label htmlFor="email" className="block mb-2.5 text-sm font-medium text-heading">Email address</label>
            <input type="email" id="email" className=" border text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-3 py-2.5 shadow-xs placeholder:text-body" placeholder="john.doe@company.com" required />
          </div>
          <div className="mb-6">
            <label htmlFor="password" className="block mb-2.5 text-sm font-medium text-heading">Password</label>
            <input type="password" id="password" className=" border text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-3 py-2.5 shadow-xs placeholder:text-body" placeholder="•••••••••" required />
          </div>
          <div className="mb-6">
            <label htmlFor="confirm_password" className="block mb-2.5 text-sm font-medium text-heading">Confirm password</label>
            <input type="password" id="confirm_password" className=" border text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-3 py-2.5 shadow-xs placeholder:text-body" placeholder="•••••••••" required />
          </div>
          <div className="flex items-start mb-6">
            <div className="flex items-center h-5">
              <input id="remember" type="checkbox" value="" className="w-4 h-4 border border-default-medium rounded-xs  focus:ring-2 focus:ring-brand-soft" required />
            </div>
            <label htmlFor="remember" className="ms-2 text-sm font-medium text-heading">I agree with the <a href="#" className="text-fg-brand hover:underline">terms and conditions</a>.</label>
          </div> */}
          <button type="submit" className="text-white bg-brand box-border border border-transparent hover:bg-brand-strong focus:ring-4 focus:ring-brand-medium shadow-xs font-medium leading-5 rounded-base text-sm px-4 py-2.5 focus:outline-none">Submit</button>
        </form>

      </>

    </div>
  )
}

export default UserCreate