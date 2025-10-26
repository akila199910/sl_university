import { cookies } from "next/headers";
import { NextResponse } from "next/server";

export async function POST(req : Request) {

    const { email, password } = await req.json();

    const res = await fetch(`http://localhost:8080/api/auth/login`, {
        method: 'POST',
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ email,  password }),
        cache: 'no-store'
    });

    const data = await res.json();

    if (!data.success || !res.ok) {
        return NextResponse.json( { success:data.success, errors: data.errors, message:data.message} , { status: res.status });
    }

    const cookieStore = await cookies();

    await cookieStore.set({
        name: 'access_token',
        value: data.data.accessToken,
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'lax',
        path: '/',
        // 1 hour default for access token in browser cookies (adjust as needed)
        maxAge: 60 * 60
    });

    await cookieStore.set({
        name: 'refreshToken',
        value: data.data.refreshToken,
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'lax',
        path: '/',
        maxAge: 60 * 60 * 24 * 7 * 4
    });

    await cookieStore.set({
        name: 'auth_user',
        value: JSON.stringify(data.data.user),
        httpOnly: false,
        secure: true,
        sameSite: 'lax',
        path: '/',
        maxAge: 60 * 60 * 24 * 7 * 4
    });
    
    return NextResponse.json({ success:data.success, data: data.data, message:data.message}, { status: res.status });
    
}