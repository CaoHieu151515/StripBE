import { Radio } from 'antd';
import { FieldValues, useController, UseControllerProps } from 'react-hook-form';
import { SelectOptions } from '../types/Selects.type';
import CustomLabel from './CustomLabel';

type CustomCheckBoxWithLabelProps<T extends FieldValues> = UseControllerProps<T> & {
  name: string;
  label: string;
  required?: boolean;
  options: SelectOptions[];
  disabled?: boolean;
};

export default function CustomCheckBoxWithLabel<T extends FieldValues>({
  name,
  control,
  label,
  required = false,
  disabled,
  options,
}: CustomCheckBoxWithLabelProps<T>) {
  const { field } = useController({
    control,
    name,
  });

  return (
    <div>
      <CustomLabel required={required} htmlFor={name} value={label} />
      <Radio.Group options={options} value={field.value} onChange={field.onChange} disabled={disabled} />
    </div>
  );
}
