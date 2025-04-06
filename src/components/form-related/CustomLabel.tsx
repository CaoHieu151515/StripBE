import { ReactNode } from 'react';
import { twMerge } from 'tailwind-merge';

type CustomLabelProps = {
  required?: boolean;
  htmlFor: string;
  value: ReactNode;
  className?: string;
};

export default function CustomLabel({ htmlFor, value, className, required = false }: CustomLabelProps) {
  return (
    <div className='flex'>
      <label className={twMerge('mb-1 block text-sm font-medium text-gray-800', className)} htmlFor={htmlFor}>
        {value}
        {required && <span className='ml-1 text-red-500'>*</span>}
      </label>
    </div>
  );
}
