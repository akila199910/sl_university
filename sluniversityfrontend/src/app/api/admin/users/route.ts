import { cookies } from 'next/headers';
import { NextResponse } from 'next/server';

export async function GET(req: Request) {
  try {
    const url = new URL(req.url);
    const qs = url.search ? url.search : '';

    const cookieStore = await cookies();
    const accessToken = cookieStore.get('access_token')?.value;

    if (!accessToken) {
      return NextResponse.json({ success: false, message: 'Unauthorized' }, { status: 401 });
    }

    const backendUrl = `${process.env.NEXT_PUBLIC_API_URL}/system_users${qs}`;

    console.log('Proxying /api/admin/users request to backend URL:', backendUrl);

    const res = await fetch(backendUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`,
      },
      cache: 'no-store',
    });

    const data = await res.json();

    if (!res.ok) {
      return NextResponse.json({ 
        success: false, 
        message: data.message || 'Backend error',
        errors: data.errors 
      }, { 
        status: res.status 
      });
    }

    return NextResponse.json({ 
      success: data.success,
      userPage: data?.data.userPage,
      availableRoles: data.data.availableRoles,
      message: data.message || 'Users fetched successfully'
    }, { 
      status: res.status,
      headers: {
        'Cache-Control': 'no-store'
      }
    });

  } catch (err: any) {
    console.error('Error in /api/admin/users proxy:', err);
    return NextResponse.json({ 
      success: false, 
      message: 'Internal server error' 
    }, { 
      status: 500 
    });
  }
}
