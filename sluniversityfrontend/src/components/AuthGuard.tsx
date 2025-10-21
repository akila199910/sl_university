"use client";
import { ReactNode } from "react";
import { useAuth } from "@/hooks/useAuth";

export default function AuthGuard({ children }: { children: ReactNode }) {
  const { user } = useAuth();
  if (user === null) {
    // you could render a spinner; navigation handled by useRequireAuth in page
    return <div className="p-6">Checking session…</div>;
  }
  return <>{children}</>;
}