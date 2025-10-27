import { cookies } from 'next/headers';
import { NextResponse } from 'next/server';

export async function GET(req: Request) {
  try {

    const url = new URL(req.url);
    const qs = url.search ? url.search : '';

    const cookieStore = await cookies();
    const accessToken = cookieStore.get('access_token')?.value;

    const backendUrl = `http://localhost:8080/api/users${qs}`;

    const res = await fetch(backendUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
      },
      cache: 'no-store',
    });

    const data = await res.json();


    if (!data.success || !res.ok) {
        return NextResponse.json( data , { status: res.status });
    }

    return NextResponse.json({ success:data.success, data: data.data, message:data.message}, { status: res.status });

  } catch (err) {
    console.error('Error in /api/admin/users proxy:', err);
    return NextResponse.json({ success: false, message: 'Internal server error' }, { status: 500 });
  }
}
