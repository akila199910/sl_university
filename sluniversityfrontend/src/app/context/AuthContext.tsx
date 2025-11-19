"use client";
import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import api from '../lib/api';

interface User {
    id: number;
    name: string;
    email: string;
    role: any,
    profileImageUrl?: string;
    coverImageUrl?: string;
}

interface LoginDTO {
  email: string;
  password: string;
}

interface AuthContextType {
  user: User | null;
  isLoading: boolean;
  login: (loginData: LoginDTO) => Promise<void>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {

  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const checkUserStatus = async () => {
      try {
        const response = await api.get('/my-profile'); 
        const loginUser: User = {
          id: response.data.data.id,
          name: response.data.data.name,
          email: response.data.data.email,
          role: response.data.data.roles,
          profileImageUrl: response.data.data.profileImageUrl,
          coverImageUrl: response.data.data.coverImageUrl,

        };
        setUser(loginUser);

      } catch (error: any) {

        setUser(null);

      } finally {
       
        setIsLoading(false);
      }
    };

    checkUserStatus();
  }, []);

  const login = async (loginData: LoginDTO) => {

    const response = await api.post('/auth/login', loginData);

        const loginUser: User = {
          id: response.data.data.id,
          name: response.data.data.name,
          email: response.data.data.email,
          role: response.data.data.roles,
          profileImageUrl: response.data.data.profileImageUrl,
          coverImageUrl: response.data.data.coverImageUrl,

        };
    
    setUser(loginUser);
  };

  const logout = async () => {
    try {
      await api.post('/auth/logout');
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      setUser(null);
      // Redirect to login after logout
      if (typeof window !== 'undefined') {
        window.location.href = '/login';
      }
    }
  };

  // Provide the state and functions to the rest of the app
  const value = {
    user,
    isLoading,
    login,
    logout,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

// --- Custom Hook (easy to use) ---
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};