import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

const PUBLIC_ROUTES = ['/login', '/register'];
const PROTECTED_ROUTES = ['/dashboard'];

export function middleware(request: NextRequest) {
  const { pathname, search } = request.nextUrl;
  const accessToken = request.cookies.get('access_token')?.value;
  const refreshToken = request.cookies.get('refreshToken')?.value;

  // If user is authenticated, prevent access to public auth routes
  // if (accessToken && PUBLIC_ROUTES.includes(pathname)) {
  //   const url = request.nextUrl.clone();
  //   url.pathname = '/dashboard';
  //   return NextResponse.redirect(url);
  // }

  // If the route is protected and there's no access token
  // if (PROTECTED_ROUTES.some((p) => pathname.startsWith(p)) && !accessToken) {
  //   const url = request.nextUrl.clone();
  //   // preserve original pathname + search as the `next` query
  //   const nextParam = pathname + (search ? `${search}` : '');

  //   if (!refreshToken) {
  //     url.pathname = '/login';
  //     url.searchParams.set('next', nextParam);
  //     return NextResponse.redirect(url);
  //   }

  //   // if refresh token exists, rewrite the request to the refresh endpoint
  //   // while keeping the intended `next` destination
  //   url.pathname = '/api/auth/refresh';
  //   url.searchParams.set('next', nextParam);
  //   return NextResponse.rewrite(url);
  // }

  return NextResponse.next();
}

// export const config = {
//   matcher: ['/login', '/register', '/dashboard/:path*'],
// };
