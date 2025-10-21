"use client";
import LoginForm from "@/components/auth/LoginForm";

export default function LoginPage() {
  

  return (
    <main className="p-6 max-w-md mx-auto">
      <h1 className="text-xl font-semibold mb-4">Sign in</h1>
      <LoginForm />
    </main>
  );
}