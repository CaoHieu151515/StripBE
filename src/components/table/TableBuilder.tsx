import { Empty, TableProps } from 'antd';
import Table, { ColumnsType } from 'antd/lib/table';
import { useEffect, useState } from 'react';
import { twMerge } from 'tailwind-merge';

interface TableBuilderProps<T extends object> {
  data: T[];
  columns: ColumnsType<T>;
  isLoading: boolean;
  rowKey: keyof T;
  selectedIds?: React.Key[];
  resetSelectedRow?: boolean;
  className?: string;
  isRowSelectable?: boolean;
  containerClassName?: string;
  onClickRow?: (record: T) => void;
  onSelectRow?(keys: React.Key[]): void;
  props?: TableProps<T>;
}

export const TableBuilder = <T extends object>({
  data,
  columns,
  rowKey,
  isLoading,
  selectedIds,
  resetSelectedRow,
  className,
  isRowSelectable = false,
  containerClassName,
  onClickRow,
  onSelectRow,
  ...props
}: TableBuilderProps<T>) => {
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>(selectedIds || []);

  const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
    setSelectedRowKeys(newSelectedRowKeys);
    onSelectRow?.(newSelectedRowKeys);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };

  useEffect(() => {
    setSelectedRowKeys([]);
  }, [resetSelectedRow]);

  useEffect(() => {
    setSelectedRowKeys(selectedIds || []);
  }, [selectedIds]);

  return (
    <div className={twMerge('relative', containerClassName)}>
      <Table
        rowKey={rowKey}
        dataSource={data}
        columns={columns}
        onRow={(record) => {
          return { onClick: () => onClickRow?.(record) };
        }}
        locale={{ emptyText: <Empty className='min-h-[400px]' /> }}
        loading={isLoading}
        pagination={false}
        scroll={props.props?.scroll ? { y: props.props.scroll.y, x: '100vw' } : { x: 'max-content' }}
        rowSelection={isRowSelectable ? rowSelection : undefined}
        className={className}
        {...props}
      />
    </div>
  );
};
