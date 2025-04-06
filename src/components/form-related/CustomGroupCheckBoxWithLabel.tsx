import { Checkbox } from 'antd';
import { FieldValues, useController, UseControllerProps } from 'react-hook-form';
import { SelectOptions } from '../types/Selects.type';
import CustomLabel from './CustomLabel';

type CustomGroupCheckBoxWithLabelProps<T extends FieldValues> = UseControllerProps<T> & {
  name: string;
  label: string;
  required?: boolean;
  options: SelectOptions[];
  disabled?: boolean;
  className?: string;
};

export default function CustomGroupCheckBoxWithLabel<T extends FieldValues>({
  name,
  control,
  label,
  required = false,
  options,
  className,
  disabled,
}: CustomGroupCheckBoxWithLabelProps<T>) {
  const { field } = useController({
    control,
    name,
  });

  return (
    <div className={className}>
      <CustomLabel required={required} htmlFor={name} value={label} />
      <Checkbox.Group options={options} value={field.value} onChange={field.onChange} disabled={disabled} />
    </div>
  );
}
