import { ReactElement, cloneElement } from 'react';
import { twMerge } from 'tailwind-merge';

type SideBarIconProps = { className?: string; icon: ReactElement };

export default function SideBarIcon({ icon, className }: SideBarIconProps) {
  return cloneElement(icon, { className: twMerge('h-5 w-5 shrink-0', className) });
}
