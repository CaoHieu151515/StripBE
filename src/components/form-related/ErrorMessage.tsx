import { resources } from '@/i18n/resources';
import { HTMLAttributes, useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';
// import ErrorMessageIcon from '../icons/ErrorMessageIcon';

type CombineAll<T> = T extends { [name in keyof T]: infer Type } ? Type : never;

type PropertyNameMap<T, IncludeIntermediate extends boolean> = {
  [name in keyof T]: T[name] extends object
    ? SubPathsOf<name, T, IncludeIntermediate> | (IncludeIntermediate extends true ? name : never)
    : name;
};

type PathsOf<T, IncludeIntermediate extends boolean = false> = CombineAll<PropertyNameMap<T, IncludeIntermediate>>;

type SubPathsOf<key extends keyof T, T, IncludeIntermediate extends boolean> = `${string & key}.${string &
  PathsOf<T[key], IncludeIntermediate>}`;

export type ErrorMessageTranslateKey = PathsOf<(typeof resources)['en']['form']['validation']> | undefined;

type MyFieldErrorProps = Pick<HTMLAttributes<HTMLDivElement>, 'className'> & {
  translateKey?: ErrorMessageTranslateKey;
  message?: string;
  noWrap?: boolean;
};

export const ErrorMessage = ({ translateKey, message, noWrap = false, className }: MyFieldErrorProps) => {
  const { t: tForm } = useTranslation('form');

  const isInvisible = useMemo(() => translateKey === undefined && !message, [message, translateKey]);

  return (
    <div
      className={twMerge(
        'mt-1 flex w-full gap-0.5 text-sm font-normal text-red-600',
        className,
        isInvisible && 'invisible',
      )}>
      {/* <ErrorMessageIcon className='mr-1 mt-[3.5px] h-3.5 w-3.5 shrink-0 text-primary-500' /> */}
      <span className={noWrap ? 'whitespace-nowrap' : ''}>
        {isInvisible
          ? 'Invalid'
          : message
            ? message
            : translateKey
              ? tForm(`validation.${translateKey}`).replace('validation.', '')
              : 'Invalid'}
      </span>
    </div>
  );
};
