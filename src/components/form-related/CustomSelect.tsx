import { Select } from 'antd';
import React from 'react';
import { Controller } from 'react-hook-form';
import { TranslationOption } from '../types/TranslationOption.type';
import CustomLabel from './CustomLabel';

const { Option } = Select;

type CustomSelectProps = {
  name: string;
  control: any;
  label?: string;
  placeholder?: string;
  options: TranslationOption[];
  required?: boolean;
  className?: string;
};

const CustomSelect: React.FC<CustomSelectProps> = ({
  name,
  control,
  label,
  placeholder,
  options,
  required = false,
  className = '',
}) => {
  return (
    <div className={className}>
      {label && <CustomLabel required={required} htmlFor={name} value={label} />}
      <Controller
        name={name}
        control={control}
        render={({ field, fieldState }) => (
          <>
            <Select
              {...field}
              onChange={(value) => field.onChange(value)}
              placeholder={placeholder}
              className='h-14 w-full'
              status={fieldState.error ? 'error' : undefined}>
              {options.map((option) => (
                <Option key={option.value} value={option.value}>
                  {option.label}
                </Option>
              ))}
            </Select>
            {fieldState.error && <span className='text-sm text-red-500'>{fieldState.error.message}</span>}
          </>
        )}
      />
    </div>
  );
};

export default CustomSelect;
