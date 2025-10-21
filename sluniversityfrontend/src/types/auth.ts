export type LoginDto = { email: string; password: string };
export type RegisterDto = { email: string; password: string };

export type TokenPair = { accessToken: string; tokenType: string };
export type AuthResponse = { message: string; success: boolean; data: TokenPair };
export type User = { id: number; email: string; role: string };