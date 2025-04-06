import { SORT_DIRECTION } from '@/utils/enum/sort-direction.enum';
import * as React from 'react';
import { twMerge } from 'tailwind-merge';
import SortAscendingIcon from '../icons/SortAscendingIcon';
import SortDescendingIcon from '../icons/SortDescendingIcon';
import { useTableUtil } from './TableContextUtil';

interface TableHeaderCellProps extends React.DetailedHTMLProps<React.ThHTMLAttributes<any>, any> {
  label: string | React.ReactNode;
  sortKey?: string;
  labelClassName?: string;
}

export const TableHeaderCell: React.FC<TableHeaderCellProps> = ({
  label,
  sortKey,
  labelClassName,
  className,
  ...rest
}) => {
  const { sortOrder, handleOnChangeOrderField } = useTableUtil();

  return (
    <button
      className={twMerge('w-full cursor-pointer bg-primary-500 p-4 text-start', className)}
      {...rest}
      onClick={() => {
        if (sortKey) handleOnChangeOrderField(sortKey);
      }}>
      <div className='relative inline-block'>
        <span className={twMerge('font-semibold capitalize leading-[18px] text-white', labelClassName)}>{label}</span>
        {Boolean(sortKey) && (
          <div className='absolute left-full top-0'>
            {sortOrder === SORT_DIRECTION.ASC ? (
              <SortAscendingIcon className={`ml-2 inline-block size-4 text-white`} />
            ) : (
              <SortDescendingIcon className={`ml-2 inline-block size-4 text-white`} />
            )}
          </div>
        )}
      </div>
    </button>
  );
};
