import { InputProps } from 'antd';
import { TextAreaProps } from 'antd/es/input';
import { ReactNode } from 'react';
import { FieldValues } from 'react-hook-form';
import CustomLabel from './CustomLabel';
import CustomTextField, { CustomTextFieldType } from './CustomTextField';

type CustomTextFieldWithLabelType<T extends FieldValues> = {
  label: ReactNode;
  required?: boolean;
  classNameInput?: string;
} & CustomTextFieldType<T> &
  InputProps &
  TextAreaProps;

// Close to customize UI

export default function CustomTextFieldWithLabel<T extends FieldValues>({
  control,
  name,
  className,
  classNameInput,
  required,
  type = 'text',
  label = '',
  placeholder = '',
  ...rest
}: CustomTextFieldWithLabelType<T>) {
  return (
    <div className={className}>
      <CustomLabel required={required} htmlFor={name} value={label} />
      <CustomTextField
        name={name}
        placeholder={placeholder}
        control={control}
        type={type}
        className={classNameInput}
        {...rest}
      />
    </div>
  );
}
