import { ReactNode } from 'react';

export default function CustomLabelChildren({ children }: { children: ReactNode }) {
  return (
    <div className='flex'>
      {children}
      <span className='ml-1 text-primary-700'>*</span>
    </div>
  );
}
