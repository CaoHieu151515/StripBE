import CustomLabel from '@/components/form-related/CustomLabel';
import { ErrorMessage, ErrorMessageTranslateKey } from '@/components/form-related/ErrorMessage';
import { Select } from 'antd';
import { SelectProps } from 'antd/lib';
import { HTMLAttributes, useState } from 'react';
import { FieldValues, UseControllerProps, useController } from 'react-hook-form';

type CustomSelectWithLabelProps<T extends FieldValues> = UseControllerProps<T> &
  Pick<HTMLAttributes<HTMLDivElement>, 'className'> & {
    options: SelectProps['options'];
    label?: string;
    required?: boolean;
    placeholder?: string;
    style?: React.CSSProperties;
    mutipleMode?: boolean;
  };

export default function CustomSelectWithLabel<T extends FieldValues>({
  name,
  control,
  className,
  options,
  label,
  placeholder,
  required = false,
  style,
  mutipleMode = false,
  ...props
}: CustomSelectWithLabelProps<T>) {
  const { field, fieldState } = useController({ control, name });
  const [searchValue, setSearchValue] = useState('');

  return (
    <div className={className}>
      <CustomLabel required={required} htmlFor={name} value={label} />
      <Select
        id={name}
        mode={mutipleMode ? 'multiple' : undefined}
        className='w-full rounded-lg text-base font-normal'
        placeholder={placeholder}
        onChange={field.onChange}
        onBlur={field.onBlur}
        value={field.value}
        optionLabelProp='label'
        maxTagCount={3}
        allowClear
        autoClearSearchValue
        optionFilterProp='label'
        onSearch={(value: string) => setSearchValue(value.slice(0, 8))}
        searchValue={searchValue}
        options={options}
        optionRender={(option) => <div className='py-0.5 text-base font-normal'>{option.data.label}</div>}
        size='large'
        style={{ ...style, fontFamily: 'inherit' }}
        {...props}
      />
      <ErrorMessage translateKey={fieldState.error?.message as ErrorMessageTranslateKey} />
    </div>
  );
}
