import { ReactNode } from 'react';

export default function FullScreenLayout({ children }: { children: ReactNode }) {
  return (
    <div className='flex min-h-screen w-screen flex-col items-center justify-center overflow-hidden bg-gray-50'>
      {children}
    </div>
  );
}
