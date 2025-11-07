import { cookies } from "next/headers";
import { NextResponse } from "next/server";

export async function GET(req:any) {
    const url = new URL(req.url);
    const next = url.searchParams.get('next') || '/dashboard';

    const cookieStore = await cookies();
    const refreshToken = cookieStore.get('refreshToken')?.value;
    console.log('Refresh token from cookie:', refreshToken);

    if(!refreshToken){
        return NextResponse.redirect(new URL('/login', req.url).toString());
    }

    const requestBody = { refreshToken, refresh_token: refreshToken };

    const backendUrl = `${process.env.NEXT_PUBLIC_API_URL}/auth/refresh?refreshToken=${encodeURIComponent(refreshToken)}`;
    const res = await fetch(backendUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${refreshToken}`,
            'X-Refresh-Token': refreshToken,
            'Cookie': `refreshToken=${refreshToken}`,
        },
        body: JSON.stringify(requestBody),
        cache: 'no-store',
    });

    let data;
    try {
        data = await res.json();
    } catch (err) {
        console.error('Failed to parse refresh response JSON', err);
        data = { success: false, message: 'Invalid JSON from auth server' };
    }

    console.log('Refresh response status:', res.status);
    console.log('Refresh response data:', data);
    if (!data.success || !res.ok) {
    const redirectUrl = NextResponse.redirect(new URL('/login', req.url).toString());
        // clear cookies on failed refresh
        redirectUrl.cookies.set('access_token', '', { maxAge: 0, path: '/' });
    redirectUrl.cookies.set('refreshToken', '', { maxAge: 0, path: '/' });
        redirectUrl.cookies.set('auth_user', '', { maxAge: 0, path: '/' });
        return redirectUrl;
    }

    const redirectUrl = NextResponse.redirect(new URL(next, req.url).toString());

    // set refreshed access token on the response
    redirectUrl.cookies.set('access_token', data.data, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'lax',
        // 1 hour
        maxAge: 6,
        path: '/',
    });

    return redirectUrl;
}