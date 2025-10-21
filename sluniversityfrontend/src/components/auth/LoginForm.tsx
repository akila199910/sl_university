"use client";
import { useState } from "react";
import { useAuth } from "@/hooks/useAuth";
import { useRouter } from "next/navigation";

export default function LoginForm() {
  const { login } = useAuth();
  const router = useRouter();
  const [email, setEmail] = useState(""); 
  const [password, setPassword] = useState("");
  const [err, setErr] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true); setErr(null);
    try {
      await login(email, password);
      router.replace("/dashboard");
    } catch (e: any) {
      console.log(e);
      setErr("Invalid credentials");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={onSubmit} className="space-y-3">
      <input value={email} onChange={e=>setEmail(e.target.value)} placeholder="Email" className="border p-2 w-full" />
      <input value={password} onChange={e=>setPassword(e.target.value)} placeholder="Password" type="password" className="border p-2 w-full" />
      {err && <p className="text-red-600 text-sm">{err}</p>}
      <button disabled={loading} className="bg-black text-white px-4 py-2">
        {loading ? "Signing in..." : "Sign in"}
      </button>
    </form>
  );
}
