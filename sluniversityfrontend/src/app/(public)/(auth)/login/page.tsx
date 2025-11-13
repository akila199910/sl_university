"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { useAuth } from "@/app/context/AuthContext";

type LoginErrors = {
  email?: string;
  password?: string;
  message?: string;
};


export default function LoginPage() {

  const router = useRouter();
  const { login , user} = useAuth();
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState<LoginErrors | null>(null);

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    try {

      await login(form);
      router.push("/dashboard");
      console.log("Logged in user:", user);

    }
    catch (err: any) {
      console.log(err);
      const errors = err?.response?.data?.errors ?? err?.response?.data ?? err?.message;
      setError(typeof errors === 'string' ? { message: errors } : errors);
    }

  }


  return (
    <main className="p-6 max-w-md mx-auto">
      <h1 className="text-xl font-semibold mb-4">Sign in</h1>
      {/* <LoginForm /> */}

      <form onSubmit={onSubmit} className="space-y-4">
        <div>
          <label className="block mb-1" htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}

            className="w-full border px-3 py-2"
          />
          {error?.email && <p className="text-red-500">{error.email}</p>}
        </div>
        <div>
          <label className="block mb-1" htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })}

            className="w-full border px-3 py-2"
          />
          {error?.password && <p className="text-red-500">{error.password}</p>}
        </div>
        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 rounded"
        >
          Login
        </button>
      </form>
    </main>
  );
}