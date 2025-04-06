/* eslint-disable react-hooks/exhaustive-deps */
import { Select, Skeleton } from 'antd';
import queryString from 'query-string';
import { ReactNode, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useLocation, useNavigate } from 'react-router-dom';
import { twMerge } from 'tailwind-merge';
import { SelectOptions } from '../types/Selects.type';
import CustomLabel from './CustomLabel';

type CustomSelectWithLabelProps = {
  queryKey: string;
  label?: string;
  options?: SelectOptions[];
  placeholder?: string;
  className?: string;
  defaultValue?: string;
  isFetching?: boolean;
  isDisable?: boolean;
  showSearch?: boolean;
  selectContainerClassName?: string;
  relatedQueryKeys?: string[];
  onSelect?: (value: string) => void;
  tooltip?: ReactNode;
};

const { Option } = Select;

export default function CustomSelectQueryWithLabel({
  queryKey,
  label,
  options,
  placeholder,
  defaultValue,
  isFetching,
  showSearch = false,
  isDisable,
  className,
  selectContainerClassName,
  relatedQueryKeys = [],
  onSelect,
}: CustomSelectWithLabelProps) {
  const navigate = useNavigate();
  const location = useLocation();
  const params = queryString.parse(location.search);
  const query = params[queryKey] || '';
  const { t: tForm } = useTranslation('form');

  useEffect(() => {
    if (defaultValue) {
      params[queryKey] = defaultValue;
      navigate({ search: queryString.stringify(params) });
    }
  }, [defaultValue, navigate, queryKey]);

  const handleDisplayDefaultValue = () => {
    if (query) return options?.find((option) => option.value.toString() === (query as string))?.label ?? '';

    if (defaultValue) return defaultValue;
  };

  return (
    <div className={twMerge('flex flex-col', className)}>
      {!!label && <CustomLabel htmlFor={queryKey} value={label} required={false} className='text-sm' />}
      <div className={twMerge('w-full', selectContainerClassName)}>
        <Select
          value={handleDisplayDefaultValue()}
          placeholder={
            <span className='flex items-center gap-2 text-sm'>{placeholder || tForm('placeholder.select')}</span>
          }
          showSearch={showSearch}
          allowClear
          filterOption={(input, option) =>
            (option?.label as string).toLowerCase().indexOf(input.toLowerCase()) >= 0 ||
            (option?.value as string).toLowerCase().indexOf(input.toLowerCase()) >= 0
          }
          onChange={(e) => {
            onSelect?.(e);
            params[queryKey] = e;

            if (relatedQueryKeys?.length) {
              for (const key of relatedQueryKeys) {
                delete params[key];
              }
            }

            navigate({ search: queryString.stringify(params) });
          }}
          className={'h-10 w-full min-w-32'}
          disabled={isDisable}>
          {isFetching
            ? Array.from({ length: 2 }).map((_, index) => (
                <Option key={index} value={index}>
                  <Skeleton active paragraph={{ rows: 1 }} />
                </Option>
              ))
            : options &&
              options.map((item, index) => (
                <Option key={index} value={item.value} label={item.label}>
                  {item.label}
                </Option>
              ))}
        </Select>
      </div>
    </div>
  );
}
