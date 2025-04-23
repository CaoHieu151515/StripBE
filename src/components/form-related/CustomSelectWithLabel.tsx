import CustomLabel from '@/components/form-related/CustomLabel';
import { ErrorMessage, ErrorMessageTranslateKey } from '@/components/form-related/ErrorMessage';
import { Select } from 'antd';
import { HTMLAttributes } from 'react';
import { FieldValues, UseControllerProps, useController } from 'react-hook-form';

export interface OptionType {
  label: string;
  value: string | number | boolean;
  disabled?: boolean;
  [key: string]: any;
}

type CustomSelectWithLabelProps<T extends FieldValues> = UseControllerProps<T> &
  Pick<HTMLAttributes<HTMLDivElement>, 'className'> & {
    options: OptionType[];
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
        options={options}
        optionRender={(option) => <div className='py-0.5 text-base font-normal'>{option.data.label}</div>}
        size='large'
        style={{ ...style, fontFamily: 'inherit', height: '55.5px' }}
        {...props}
      />
      <ErrorMessage translateKey={fieldState.error?.message as ErrorMessageTranslateKey} />
    </div>
  );
}
